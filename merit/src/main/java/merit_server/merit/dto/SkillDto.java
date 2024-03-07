package merit_server.merit.dto;

import lombok.Getter;
import lombok.Setter;
import merit_server.merit.domain.Skill;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class SkillDto  {
    private Long id;
    private String name;
    private String skillsDescription;

    private static ModelMapper modelMapper = new ModelMapper();

    // convert Entity to Dto
    public static SkillDto from(Skill skill) {
        return modelMapper.map(skill, SkillDto.class);
    }
}