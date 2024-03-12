package com.merit.mapper;

import lombok.RequiredArgsConstructor;
import com.merit.domain.Skill;
import com.merit.dto.SkillDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SkillMapper {
    private final ModelMapper modelMapper;

    public Skill to(SkillDto skillDto) {
        return modelMapper.map(skillDto, Skill.class);
    }

    // convert Entity to Dto
    public SkillDto from(Skill skill) {
        return modelMapper.map(skill, SkillDto.class);
    }

}
