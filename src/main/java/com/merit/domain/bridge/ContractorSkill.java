package com.merit.domain.bridge;

import com.merit.domain.Contractor;
import com.merit.domain.Skill;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
public class ContractorSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTRACTOR_SKILLS_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SKILLS_ID")
    private Skill skill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACTOR_ID")
    private Contractor contractor;

    public void addSkill(Skill skill) {
        this.skill = skill;
        skill.getContractorSkills().add(this);
    }

    public void addContractor(Contractor contractor) {
        this.contractor = contractor;
        contractor.getContractorSkills().add(this);
    }

    public void removeContractor(Contractor contractor) {
        this.contractor = null;
        contractor.getContractorSkills().remove(this);
    }

    public void removeSkill(Skill skill) {
        this.skill = null;
        skill.getContractorSkills().remove(this);
    }
}
