package merit_server.merit.domain;

import jakarta.persistence.*;
import lombok.*;
import merit_server.merit.dto.CompanyContractorDto;
import merit_server.merit.dto.ProjectDto;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class CompanyContractor {

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

    private LocalDateTime createdOn;

    private CompanyContractorStatus status;
    private LocalDateTime lastStatusDate;

    public void addCompany(Company company) {
        this.company = company;
        company.getCompanyContractors().add(this);
    }

    public void addContractor(Contractor contractor) {
        this.contractor = contractor;
        contractor.getCompanyContractors().add(this);
    }

    private static ModelMapper modelMapper = new ModelMapper();

    public static CompanyContractor to(CompanyContractorDto companyContractorDto) {
        return modelMapper.map(companyContractorDto, CompanyContractor.class);
    }


}
