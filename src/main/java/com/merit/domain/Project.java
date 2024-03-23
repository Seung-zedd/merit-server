package com.merit.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.merit.domain.bridge.ProjectContractor;
import com.merit.domain.bridge.ProjectSkill;
import com.merit.domain.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PROJECTS")
@Getter
@Builder(toBuilder = true)
// 필드명이 많으면 더러워지니까 그냥 오버라이딩으로 로그를 보자
@ToString(of = {"id", "name", "projectDescription", "role", "minExpReqd", "maxExpReqd", "salaryRange", "status", "createdBy"})
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
    @Setter
    private ProjectStatus status;

    private String createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    @JsonBackReference // toString 순환참조 방지 위해 연관관계의 주인이 되는 엔티티에 붙임
    private Company company;

    @OneToMany(mappedBy = "project")
    @JsonManagedReference // mappedBy 있는 곳에 붙임
    @Builder.Default
    private List<ProjectContractor> projectContractors = new ArrayList<>();

    // 특정 기간이 지나면 project의 상태를 CLOSED로 변경할 것
    public void changeStatus(ProjectStatus newStatus) {
        this.setStatus(newStatus);
    }

    public void addCompany(Company company) {
        this.company = company;
        company.getProjects().add(this);
    }

    public void removeCompany(Company company) {
        this.company = null;
        company.getProjects().remove(this);
    }

    /* business logic for updating domain*/
}
