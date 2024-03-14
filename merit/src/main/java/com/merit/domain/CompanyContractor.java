package com.merit.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class CompanyContractor extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPANY_CONTRACTOR_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACTOR_ID")
    private Contractor contractor;

    private CompanyContractorStatus status;

    // relationships are replaced with firebase
   /* public void addCompany(Company company) {
        this.company = company;
        company.getCompanyContractors().add(this);
    }

    public void addContractor(Contractor contractor) {
        this.contractor = contractor;
        contractor.getCompanyContractors().add(this);
    }*/

}
