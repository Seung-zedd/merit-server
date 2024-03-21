package com.merit.service;

import com.merit.domain.*;
import com.merit.dto.SkillDto;
import com.merit.mapper.SkillMapper;
import com.merit.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.merit.dto.ContractorDto;
import com.merit.mapper.ContractorMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static graphql.introspection.IntrospectionQueryBuilder.build;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContractorService {
    private final CompanyRepository companyRepository;

    private final ContractorSkillRepository contractorSkillRepository;
    private final SkillRepository skillRepository;
    private final ContractorRepository contractorRepository;

    //used for SMTP service
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;

    // * (Create)Employer should be able to create an account for freelancers
    //! this should be called after accepting invitation email, that is, if the CompanyStatus set onto ACCEPTED
    // 이미 ProjectContractorService에서 연관관계 매핑하였음
    @Transactional
    public ResponseEntity<?> createContractor(ContractorDto contractorDto, List<SkillDto> skillDtos, Long companyId) {

        // create Contractor entity
        Contractor contractor = ContractorMapper.INSTANCE.to(contractorDto);

        List<Skill> skills = skillDtos.stream()
                .map(SkillMapper.INSTANCE::to)
                .toList();

        skillRepository.saveAll(skills);
        contractor.setStatus(ContractorStatus.AVAILABLE);

        // verify if the contractorEmail is already in use
        if (contractorRepository.existsByContractorEmail(contractor.getContractorEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        Contractor savedContractor = contractorRepository.save(contractor);

        // Contractor-Skill
        for (Skill skill : skills) {
            ContractorSkill contractorSkill = new ContractorSkill();
            contractorSkill.addContractor(contractor);
            contractorSkill.addSkill(skill);

            contractorSkillRepository.save(contractorSkill);
        }

        // Contractor-Company, add Contractor account after confirming Verification mail
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));
        savedContractor.addCompany(company);

        // about EmailService
        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(contractor);

        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(contractor.getContractorEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);

        System.out.println("Confirmation Token: " + confirmationToken.getConfirmationToken());

        return ResponseEntity.ok("Verify email by the link sent on your email address");
    }

    // * (Confirmation)
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationTokenEntity token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            Contractor contractor = contractorRepository.findByContractorEmailIgnoreCase(token.getContractor().getContractorEmail());
            contractor.setEnabled(true);
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }

    // * (Read)
    public ContractorDto getContractor(Long id) {
        Contractor contractor = contractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contractor not found with id: " + id));
        return ContractorMapper.INSTANCE.from(contractor);
    }

    // * (Read) should be able to view contractor listing
    public List<ContractorDto> getAllContractors() {
        List<Contractor> contractors = contractorRepository.findAll();
        return contractors.stream()
                .map(ContractorMapper.INSTANCE::from)
                .toList();
    }

    // * (Read-Condition) Employer should be able to view complete Contractor list available on the system.
    public List<ContractorDto> getAvailableContractors() {
        List<Contractor> allByStatus = contractorRepository.findAllByStatus(ContractorStatus.AVAILABLE);
        return allByStatus.stream()
                .map(ContractorMapper.INSTANCE::from)
                .toList();
    }

    // * (Update)
    @Transactional
    public Long updateContractor(Long id, ContractorDto contractorDto, List<SkillDto> skillDtos) {
        Contractor contractor = contractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contractor not found with id: " + id));

        Address updatedAddress = Address.builder()
                .city(contractorDto.getAddress().getCity())
                .street(contractorDto.getAddress().getStreet())
                .zipcode(contractorDto.getAddress().getZipcode())
                .build();

        // should update skills only related to Contractor
        List<ContractorSkill> contractorSkills = contractor.getContractorSkills();

        for (ContractorSkill contractorSkill : contractorSkills) {

            Skill skill = contractorSkill.getSkill();

            for (SkillDto skillDto : skillDtos) {
                Skill updatedSkill = skill.toBuilder()
                        .name(skillDto.getName())
                        .skillsDescription(skillDto.getSkillsDescription())
                        .build();
                skillRepository.save(updatedSkill);
                log.debug("updatedSkill={}", updatedSkill);
            }
        }

        Contractor updatedContractor = contractor.toBuilder()
                .name(contractorDto.getName())
                .contractorEmail(contractorDto.getContractorEmail())
                .website(contractorDto.getWebsite())
                .status(contractorDto.getStatus())
                .address(updatedAddress)
                .avatar(new Image(contractorDto.getAvatar().getFileName()))
                .resume(new PdfDocument(contractorDto.getResume().getPdfFileName()))
                .contactNumber(contractorDto.getContactNumber())
                .expectedPay(contractorDto.getExpectedPay())
                .expectedPayCurrency(contractorDto.getExpectedPayCurrency())
                .build();

        Contractor savedContractor = contractorRepository.save(updatedContractor);
        log.debug("savedContractor={}", savedContractor);
        return savedContractor.getId();

    }

    // * (Delete)
    //? 삭제는 ProjectService 참조할 것 <- Contractor-skill 연관관계 제거
    @Transactional
    public void deleteContractor(Long id, Long companyId) {
        Contractor contractor = contractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contractor not found with id: " + id));

        List<ContractorSkill> contractorSkillsToDelete = new ArrayList<>();

        for (ContractorSkill contractorSkill : new ArrayList<>(contractor.getContractorSkills())) {

            Skill skill = contractorSkill.getSkill();
            contractorSkill.removeContractor(contractor);
            contractorSkill.removeSkill(skill);
            contractorSkillsToDelete.add(contractorSkill);
        }

        contractorSkillRepository.deleteAll(contractorSkillsToDelete);

        Company company = companyRepository.findById(companyId).orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));
        contractor.removeCompany(company);

        contractorRepository.deleteById(id);
    }
}
