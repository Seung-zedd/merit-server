package com.merit.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.merit.domain.bridge.ContractorSkill;
import com.merit.domain.bridge.ProjectContractor;
import com.merit.domain.embedded.Address;
import com.merit.domain.embedded.Image;
import com.merit.domain.embedded.PdfDocument;
import com.merit.domain.enums.ContractorStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Getter
public class Contractor extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTRACTOR_ID")
    private Long id;

    @Column(name = "CONTRACTOR_NAME", unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    private String website;

    @Enumerated(EnumType.STRING)
    @Setter
    private ContractorStatus status;

    @Embedded
    private Address address;
    private String contactNumber;

    @OneToMany(mappedBy = "contractor")
    @Builder.Default
    private List<ContractorSkill> contractorSkills = new ArrayList<>();

    private int experience;
    private int expectedPay;
    private String expectedPayCurrency;

    // * file 타입의 resume을 저장한다
    @Embedded
    private PdfDocument resume;

    @Embedded
    private Image avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    @JsonBackReference // toString 순환참조 방지 위해 연관관계의 주인이 되는 엔티티에 붙임
    private Company company;

    // *feat: this will be used our MVP proudct as well as database
    // the Contractor can update their projects having been worked on our software
    @OneToMany(mappedBy = "contractor")
    @Builder.Default
    private List<ProjectContractor> projectContractors = new ArrayList<>();

    public void addCompany(Company company) {
        this.company = company;
        company.getContractors().add(this);
    }

    public void removeCompany(Company company) {
        this.company = null;
        company.getContractors().remove(this);
    }

    // * Contractor can change their status when they want to
    public void changeStatus(ContractorStatus newStatus) {
        this.setStatus(newStatus);
    }

}
