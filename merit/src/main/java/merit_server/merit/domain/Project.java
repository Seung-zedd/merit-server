package merit_server.merit.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "PROJECT")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID")
    private Long id;

    @Column(name = "PROJECT_NAME")
    private String name;
    private String projectDescription;

    // * skills required for the project
    @ElementCollection
    @CollectionTable(
            name = "SKILL_LIST_RQ",
            joinColumns = @JoinColumn(name = "PROJECT_ID"))
    @Column(name = "SKILL_NAME")
    @Builder.Default
    private Set<String> skillList = new HashSet<>();

    private int minExpReqd;
    private int maxExpReqd;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private String createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @OneToMany(mappedBy = "project")
    @Column(name = "APPLICATIONS")
    @Builder.Default
    private List<Application> applications = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    @Column(name = "HIRED_CONTRACTORS")
    @Builder.Default
    private List<Contractor> contractors = new ArrayList<>();

    private int expectedPay;
    private String expectedPayCurrency;

    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;

    public void setCompany(Company company) {
        this.company = company;
        company.getProjects().add(this);
    }
}
