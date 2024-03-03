package merit_server.merit.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "CONTRACTOR")
@Data
public class Contractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTRACTOR_ID")
    private Long id;

    @Column(name = "CONTRACTOR_NAME")
    private String name;
    private String email;
    private String website;

    @Enumerated(EnumType.STRING)
    private ContractorStatus status;

    @Embedded
    private Address address;
    private String contactNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @ElementCollection
    @CollectionTable(
            name = "SKILL_LIST",
            joinColumns = @JoinColumn(name = "CONTRACTOR_ID"))
    @Column(name = "SKILL_NAME")
    private Set<String> skillList = new HashSet<>();

    private int experience;

    @OneToMany(mappedBy = "contractor")
    private List<Company> companyList = new ArrayList<>();

    private int expectedPay;
    private String expectedPayCurrency;

    // * file 타입의 resume을 저장한다
    @Embedded
    private PdfDocument resume;

    @Embedded
    private Image avatar;

    @OneToMany(mappedBy = "contractor")
    private List<ContractorApplication> caList = new ArrayList<>();
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;
}
