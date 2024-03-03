package merit_server.merit.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "COMPANY")
@Data
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACTOR_ID")
    private Contractor contractor;

    @OneToMany(mappedBy = "company")
    private List<Project> projectList = new ArrayList<>();
    private LocalDateTime createdOn;
}
