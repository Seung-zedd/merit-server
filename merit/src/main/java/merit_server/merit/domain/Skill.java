package merit_server.merit.domain;

import jakarta.persistence.*;
import lombok.*;
import merit_server.merit.dto.SkillDto;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SKILLS_ID")
    private Long id;

    @Column(name = "SKILLS_NAME")
    private String name;

    private String skillsDescription;

    @OneToMany(mappedBy = "skill")
    @Builder.Default
    private List<ContractorSkill> contractorSkills = new ArrayList<>();

    @OneToMany(mappedBy = "skill")
    @Builder.Default
    private List<ProjectSkill> projectSkills = new ArrayList<>();
}
