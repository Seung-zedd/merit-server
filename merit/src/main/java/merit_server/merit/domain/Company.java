package merit_server.merit.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Company {

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
    @Builder.Default
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    @Builder.Default
    private List<CompanyContractor> companyContractors = new ArrayList<>();

    private LocalDateTime createdOn;

}
