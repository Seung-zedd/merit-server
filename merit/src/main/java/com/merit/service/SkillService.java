package com.merit.service;

import com.merit.domain.Skill;
import com.merit.dto.SkillDto;
import com.merit.mapper.SkillMapper;
import com.merit.repository.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    // * (Create) 이미 Project, Contractor 쪽에 연관관계 편의 메서드를 작성했으므로 스킬만 만들면 된다
    @Transactional
    public Long createSkill(SkillDto skillDto) {

        // create Skill entity
        Skill skill = skillMapper.to(skillDto);
        Skill savedSkill = skillRepository.save(skill);

        return savedSkill.getId();
    }

    // * (Read) should read Skill's detail
    public SkillDto getSkill(Long id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("SKill not found with id: " + id));
        return skillMapper.from(skill);
    }

    // * (Read) skill listing
    public List<SkillDto> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();
        return skills.stream()
                .map(skillMapper::from)
                .toList();
    }

    // * (Update)
    @Transactional
    public Long updateSkill(Long id, SkillDto skillDto) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Skill not found with id: " + id));

        Skill updatedSKill = skill.toBuilder()
                .name(skillDto.getName())
                .skillsDescription(skillDto.getSkillsDescription())
                .build();
        Skill savedSkill = skillRepository.save(updatedSKill);
        return savedSkill.getId();
    }


}
