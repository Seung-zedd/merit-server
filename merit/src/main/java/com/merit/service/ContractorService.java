package com.merit.service;

import com.merit.domain.*;
import com.merit.dto.CompanyContractorDto;
import com.merit.dto.CompanyDto;
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

import java.util.List;

import static graphql.introspection.IntrospectionQueryBuilder.build;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContractorService {

    private final ProjectRepository projectRepository;

    private final ProjectContractorRepository projectContractorRepository;
    private final ContractorSkillRepository contractorSkillRepository;
    private final SkillRepository skillRepository;
    private final ContractorRepository contractorRepository;

    private final ContractorMapper contractorMapper;
    private final SkillMapper skillMapper;

    // * (Create)Employer should be able to create an account for freelancers
    //! 연관관계 편의 메서드: ContractorSkill가 필요
    //! => Company가 Contractor를 초대하기 때문에 파이어베이스 대신 이용
    //? Contractor와 Project 간에도 cascade를 켜야할까? -> NO. Contractor가 삭제되도 얘가 어떤 Project를 맡았는지 정보가 필요할 수도 있음
    @Transactional
    public Long createContractor(ContractorDto contractorDto, List<SkillDto> skillDtos, List<Project> projects) {

        // create Contractor entity
        Contractor contractor = contractorMapper.to(contractorDto);
        Contractor savedContractor = contractorRepository.save(contractor);

        // Save Contractor-Skill relationship
        for (SkillDto skillDto : skillDtos) {
            Skill skill = skillMapper.to(skillDto);

            // Check if Skill already exists in database
            Skill existingSkill = skillRepository.findByName(skill.getName());
            if (existingSkill != null) {
                skill = existingSkill; // Use existing Skill if found
            } else {
                // Save new Skill if not found
                skill = skillRepository.save(skill);
            }

            // Create and save Contractor-Skill relationship
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
        return contractorMapper.from(contractor);
    }

    // * (Read) should be able to view contractor listing
    public List<ContractorDto> getAllContractors() {
        List<Contractor> contractors = contractorRepository.findAll();
        return contractors.stream()
                .map(contractorMapper::from)
                .toList();
    }

    // * (Update)
    @Transactional
    public Long updateContractor(Long id, ContractorDto contractorDto) {
        Contractor contractor = contractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contractor not found with id: " + id));

        Address updatedAddress = Address.builder()
                .city(contractorDto.getAddress().getCity())
                .street(contractorDto.getAddress().getStreet())
                .zipcode(contractorDto.getAddress().getZipcode())
                .build();

        PdfDocument updatedResume = PdfDocument.builder()
                .pdfFileName(contractorDto.getResume().getPdfFileName())
                .pdfFileOriName(contractorDto.getResume().getPdfFileName())
                .pdfFileUrl(contractorDto.getResume().getPdfFileUrl())
                .build();

        Image updatedAvatar = Image.builder()
                .fileOriName(contractorDto.getAvatar().getFileOriName())
                .fileUrl(contractorDto.getAvatar().getFileUrl())
                .fileName(contractorDto.getAvatar().getFileName())
                .build();

        Contractor updatedContractor = contractor.toBuilder()
                .name(contractorDto.getName())
                .email(contractorDto.getEmail())
                .website(contractorDto.getWebsite())
                .status(contractorDto.getStatus())
                .address(updatedAddress)
                .avatar(updatedAvatar)
                .resume(updatedResume)
                .contactNumber(contractor.getContactNumber())
                .expectedPay(contractor.getExpectedPay())
                .expectedPayCurrency(contractor.getExpectedPayCurrency())
                .build();
        Contractor savedContractor = contractorRepository.save(updatedContractor);
        return savedContractor.getId();
    }

    // * (Delete)
    @Transactional
    public void deleteContractor(Long id) {
        contractorRepository.deleteById(id);
    }
}
