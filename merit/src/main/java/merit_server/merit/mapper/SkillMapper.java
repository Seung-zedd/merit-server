package merit_server.merit.mapper;

import lombok.RequiredArgsConstructor;
import merit_server.merit.domain.Skill;
import merit_server.merit.dto.SkillDto;
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
