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
    private String contractorEmail;
    private String website;

    @Enumerated(EnumType.STRING)
    @Setter
    private ContractorStatus status;

    @Embedded
    private Address address;
    private String contactNumber;

    @OneToMany(mappedBy = "contractor")
    @JsonManagedReference
    @Builder.Default
    private List<ContractorSkill> contractorSkills = new ArrayList<>();

    private int experience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    private int expectedPay;
    private String expectedPayCurrency;

    // * file 타입의 resume을 저장한다
    @Embedded
    private PdfDocument resume;

    @Embedded
    private Image avatar;

    @Setter
    private boolean isEnabled; //this will be used for verification mail

    // *feat: this will be used our MVP proudct as well as database
    // the Contractor can update their projects having been worked on our software
    @OneToMany(mappedBy = "contractor")
    @JsonManagedReference
    @Builder.Default
    private List<ProjectContractor> projectContractors = new ArrayList<>();

    public Contractor(String name, String contractorEmail, boolean isEnabled) {
        this.name = name;
        this.contractorEmail = contractorEmail;
        this.isEnabled = isEnabled;
    }

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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "email = " + contractorEmail + ", " +
                "website = " + website + ", " +
                "status = " + status + ", " +
                "address = " + address + ", " +
                "contactNumber = " + contactNumber + ", " +
                "experience = " + experience + ", " +
                "expectedPay = " + expectedPay + ", " +
                "expectedPayCurrency = " + expectedPayCurrency + ", " +
                "resume = " + resume + ", " +
                "avatar = " + avatar + ")";
    }
}
