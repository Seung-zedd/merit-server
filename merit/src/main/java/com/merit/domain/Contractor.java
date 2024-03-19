package com.merit.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    @Builder.Default
    private List<ContractorSkill> contractorSkills = new ArrayList<>();

    private int experience;

    @OneToMany(mappedBy = "contractor")
    @JsonManagedReference
    @Builder.Default
    private List<CompanyContractor> companyContractors = new ArrayList<>();

    private int expectedPay;
    private String expectedPayCurrency;

    // * file 타입의 resume을 저장한다
    @Embedded
    private PdfDocument resume;

    @Embedded
    private Image avatar;

    // *feat: this will be used our MVP proudct as well as database
    // the Contractor can update their projects having been worked on our software
    @OneToMany(mappedBy = "contractor")
    @JsonManagedReference
    @Builder.Default
    private List<ProjectContractor> projectContractors = new ArrayList<>();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "email = " + email + ", " +
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
