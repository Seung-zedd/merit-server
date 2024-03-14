package com.merit.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_SKILLS_ID")
    @Setter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SKILLS_ID")
    private Skill skill;

    private boolean required;

    public void addProject(Project project) {
        this.project = project;
        project.getProjectSkills().add(this);
    }

    public void addSkill(Skill skill) {
        this.skill = skill;
        skill.getProjectSkills().add(this);
    }
}
