package merit_server.merit.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID")
    private Long id;

    @Column(name = "PROJECT_NAME")
    private String name;
    private String projectDescription;

    @OneToMany(mappedBy = "project")
    @Builder.Default
    private List<ProjectSkill> projectSkills = new ArrayList<>();

    // * I autonomously added role column related to wireframe("UX Researcher")
    @Column(name = "PROJECT_ROLE")
    private String role;

    // * I think need to be added Salary column

    private int minExpReqd;
    private int maxExpReqd;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private String createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;

    @OneToMany(mappedBy = "project")
    @Builder.Default
    private List<ProjectContractor> projectContractors = new ArrayList<>();

    public void addCompany(Company company) {
        this.company = company;
        company.getProjects().add(this);
    }
}
