package merit_server.merit.domain;

import jakarta.persistence.*;
import lombok.*;
import merit_server.merit.dto.ContractorDto;
import merit_server.merit.dto.ProjectDto;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Contractor {

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
    private ContractorStatus status;

    @Embedded
    private Address address;
    private String contactNumber;

    @OneToMany(mappedBy = "contractor")
    @Builder.Default
    private List<ContractorSkill> contractorSkills = new ArrayList<>();

    private int experience;

    @OneToMany(mappedBy = "contractor")
    @Builder.Default
    private List<CompanyContractor> companyContractors = new ArrayList<>();

    private int expectedPay;
    private String expectedPayCurrency;

    // * file 타입의 resume을 저장한다
    @Embedded
    private PdfDocument resume;

    @Embedded
    private Image avatar;

    @OneToMany(mappedBy = "contractor")
    @Builder.Default
    private List<ProjectContractor> projectContractors = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "APPLICATION_ID", unique = true)
    @Setter(AccessLevel.NONE)
    private Application application;

    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static Contractor to(ContractorDto contractorDto) {
        return modelMapper.map(contractorDto, Contractor.class);
    }
}
