package com.merit.service;

import com.merit.domain.Contractor;
import com.merit.domain.Skill;
import com.merit.domain.bridge.ContractorSkill;
import com.merit.dto.SkillDto;
import com.merit.mapper.SkillMapper;
import com.merit.repository.ContractorRepository;
import com.merit.repository.ContractorSkillRepository;
import com.merit.repository.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillService {
    private final ContractorRepository contractorRepository;
    private final ContractorSkillRepository contractorSkillRepository;

    private final SkillRepository skillRepository;

    // * (Create)
    // 스킬을 생성하는 행위는 한번씩
    @Transactional
    public Long createSkill(SkillDto skillDto, Long contractorId) {

        // create Skill entity
        Skill skill = SkillMapper.INSTANCE.to(skillDto);
        Skill savedSkill = skillRepository.save(skill);

        // Skill-Contractor
        Contractor contractor = contractorRepository.findById(contractorId).orElseThrow(() -> new EntityNotFoundException("Contractor not found with id: " + contractorId));

        ContractorSkill contractorSkill = new ContractorSkill();
        contractorSkill.addContractor(contractor);
        contractorSkill.addSkill(savedSkill);

        return savedSkill.getId();
    }

    // * (Read) should read Skill's detail
    public SkillDto getSkill(Long id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Skill not found with id: " + id));
        return SkillMapper.INSTANCE.from(skill);
    }

    // * (Read) skill listing
    public List<SkillDto> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();
        return skills.stream()
                .map(SkillMapper.INSTANCE::from)
                .toList();
    }

    // * (Update)
    // should update skills only related to Contractor
    // 스킬이 업데이트되는 행위는 List<SkillDto>를 받은다음에 한번에 가능
    @Transactional
    public void updateSkill(Long id, List<SkillDto> skillDtos) {
        Contractor contractor = contractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contractor not found with id: " + id));
        List<ContractorSkill> contractorSkills = contractor.getContractorSkills();

        for (ContractorSkill contractorSkill : contractorSkills) {
            for (SkillDto skillDto : skillDtos) {
                Skill updatedSkill = contractorSkill.getSkill()
                        .toBuilder()
                        .name(skillDto.getName())
                        .skillsDescription(skillDto.getSkillsDescription())
                        .build();
                skillRepository.save(updatedSkill);
            }
        }
    }

    @Transactional
    public void deleteSkill(Long skillId, Long contractorId) {
        Skill skill = skillRepository.findById(skillId).orElseThrow(() -> new EntityNotFoundException("Skill not found with id: " + skillId));
        Contractor contractor = contractorRepository.findById(contractorId).orElseThrow(() -> new EntityNotFoundException("Contractor not found with id: " + contractorId));
        List<ContractorSkill> contractorSkillsToDelete = new ArrayList<>();

        for (ContractorSkill contractorSkill : new ArrayList<>(skill.getContractorSkills())) {

            contractorSkill.removeContractor(contractor);
            contractorSkill.removeSkill(skill);
            contractorSkillsToDelete.add(contractorSkill);
        }

        contractorSkillRepository.deleteAll(contractorSkillsToDelete);
        skillRepository.deleteById(skillId);
    }
}
