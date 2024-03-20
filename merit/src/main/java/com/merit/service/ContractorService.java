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

    private final ContractorSkillRepository contractorSkillRepository;
    private final SkillRepository skillRepository;
    private final ContractorRepository contractorRepository;

    // * (Create)Employer should be able to create an account for freelancers
    //! 연관관계 편의 메서드: ContractorSkill가 필요
    //! => Company가 Contractor를 초대하기 때문에 파이어베이스 대신 이용
    // 이미 ProjectContractorService에서 연관관계 매핑하였음
    @Transactional
    public Long createContractor(ContractorDto contractorDto, List<SkillDto> skillDtos) {

        // create Contractor entity
        Contractor contractor = ContractorMapper.INSTANCE.to(contractorDto);

        List<Skill> skills = skillDtos.stream()
                .map(SkillMapper.INSTANCE::to)
                .toList();

        skillRepository.saveAll(skills);
        contractor.setStatus(ContractorStatus.AVAILABLE);

        Contractor savedContractor = contractorRepository.save(contractor);

        // Contractor-Skill
        for (Skill skill : skills) {
            ContractorSkill contractorSkill = new ContractorSkill();
            contractorSkill.addContractor(contractor);
            contractorSkill.addSkill(skill);

            contractorSkillRepository.save(contractorSkill);
        }

        return savedContractor.getId();
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
                .email(contractorDto.getEmail())
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
    public void deleteContractor(Long id) {
        Contractor contractor = contractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contractor not found with id: " + id));

        List<ContractorSkill> contractorSkillsToDelete = new ArrayList<>();

        for (ContractorSkill contractorSkill : new ArrayList<>(contractor.getContractorSkills())) {

            Skill skill = contractorSkill.getSkill();
            contractorSkill.removeContractor(contractor);
            contractorSkill.removeSkill(skill);
            contractorSkillsToDelete.add(contractorSkill);
        }

        contractorSkillRepository.deleteAll(contractorSkillsToDelete);

        contractorRepository.deleteById(id);
    }
}
