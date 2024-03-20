package com.merit.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMPANIES")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Getter
public class Company extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPANY_ID")
    private Long id;

    @Column(name = "COMPANY_NAME")
    private String name;
    private String email;
    private String website;

    private String contactNumber;

    @Embedded
    private Address address;
    private String about;

    @Enumerated(EnumType.STRING)
    private CompanyStatus status;

    @OneToMany(mappedBy = "company")
    @JsonManagedReference
    @Builder.Default
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    @JsonManagedReference
    @Builder.Default
    private List<CompanyContractor> companyContractors = new ArrayList<>();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + getId() + ", " +
                "createdOn = " + getCreatedOn() + ", " +
                "modifiedOn = " + getModifiedOn() + ", " +
                "name = " + getName() + ", " +
                "email = " + getEmail() + ", " +
                "website = " + getWebsite() + ", " +
                "contactNumber = " + getContactNumber() + ", " +
                "address = " + getAddress() + ", " +
                "about = " + getAbout() + ", " +
                "status = " + getStatus() + ")";
    }
}
