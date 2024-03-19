package com.merit.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID")
    private Long id;

    @Column(name = "PROJECT_NAME")
    private String name;
    private String projectDescription;

    @OneToMany(mappedBy = "project")
    @JsonManagedReference
    @Builder.Default
    private List<ProjectSkill> projectSkills = new ArrayList<>();

    // * I autonomously added role column related to wireframe("UX Researcher")
    @Column(name = "PROJECT_ROLE")
    private String role;

    // * I think need to be added Salary column

    private int minExpReqd;
    private int maxExpReqd;
    private int salaryRange;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private String createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    @JsonBackReference
    private Company company;

    @OneToMany(mappedBy = "project")
    @JsonManagedReference
    @Builder.Default
    private List<ProjectContractor> projectContractors = new ArrayList<>();

    public void addCompany(Company company) {
        this.company = company;
        company.getProjects().add(this);
    }

    public void removeCompany(Company company) {
        this.company = null;
        company.getProjects().remove(this);
    }

    /* business logic for updating domain*/






    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + getId() + ", " +
                "createdOn = " + getCreatedOn() + ", " +
                "modifiedOn = " + getModifiedOn() + ", " +
                "name = " + getName() + ", " +
                "projectDescription = " + getProjectDescription() + ", " +
                "role = " + getRole() + ", " +
                "minExpReqd = " + getMinExpReqd() + ", " +
                "maxExpReqd = " + getMaxExpReqd() + ", " +
                "status = " + getStatus() + ", " +
                "createdBy = " + getCreatedBy() + ", ";
    }
}
