package com.merit.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Getter
public class ProjectContractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_CONTRACTOR_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACTOR_ID")
    private Contractor contractor;

    @Enumerated(EnumType.STRING)
    private ProjectContractorStatus status;

    private String comment;
    private String rateType;

    private float expectedRate;
    private int expectedHoursPerWeek;
    private String expectedPayCurrency;

    private LocalDate applicationDate = LocalDate.now();

    public void addProject(Project project) {
        this.project = project;
        project.getProjectContractors().add(this);
    }

    public void addContractor(Contractor contractor) {
        this.contractor = contractor;
        contractor.getProjectContractors().add(this);
    }
}
