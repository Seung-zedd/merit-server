package com.merit.mapper;

import com.merit.domain.Skill;
import com.merit.dto.SkillDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SkillMapper {

    SkillMapper INSTANCE = Mappers.getMapper(SkillMapper.class);

    SkillDto from(Skill skill);

    Skill to(SkillDto skillDto);

}
