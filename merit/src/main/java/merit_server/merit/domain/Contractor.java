package merit_server.merit.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "CONTRACTOR")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @ElementCollection
    @CollectionTable(
            name = "SKILL_LIST",
            joinColumns = @JoinColumn(name = "CONTRACTOR_ID"))
    @Column(name = "SKILL_NAME")
    @Builder.Default
    private Set<String> skillList = new HashSet<>();

    private int experience;

    @OneToMany(mappedBy = "contractor")
    @Builder.Default
    private List<Company> companies = new ArrayList<>();

    private int expectedPay;
    private String expectedPayCurrency;

    // * file 타입의 resume을 저장한다
    @Embedded
    private PdfDocument resume;

    @Embedded
    private Image avatar;

    @OneToMany(mappedBy = "contractor", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<ContractorApplication> contractorApplications = new ArrayList<>();

    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;

    public void setProject(Project project) {
        this.project = project;
        project.getContractors().add(this);
    }
}
