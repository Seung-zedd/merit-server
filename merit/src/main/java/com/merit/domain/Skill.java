package com.merit.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SKILLS")
@Builder(toBuilder = true)
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
    @JsonManagedReference
    @Builder.Default
    private List<ContractorSkill> contractorSkills = new ArrayList<>();

    @OneToMany(mappedBy = "skill")
    @JsonManagedReference
    @Builder.Default
    private List<ProjectSkill> projectSkills = new ArrayList<>();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "skillsDescription = " + skillsDescription + ")";
    }
}
