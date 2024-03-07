package merit_server.merit.domain;

import jakarta.persistence.*;
import lombok.*;
import merit_server.merit.dto.ProjectContractorDto;
import merit_server.merit.dto.ProjectDto;
import org.modelmapper.ModelMapper;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ProjectContractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_CONTRACTOR_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACTOR_ID")
    private Contractor contractor;

    @Enumerated(EnumType.STRING)
    private ProjectContractorStatus status;

    private String comment;
    private String rateType;

    private float expectedRate;
    private int expectedHoursPerWeek;
    private String expectedPayCurrency;

    private String applicationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPLICATION_ID")
    private Application application;

    public void addProject(Project project) {
        this.project = project;
        project.getProjectContractors().add(this);
    }

    public void addContractor(Contractor contractor) {
        this.contractor = contractor;
        contractor.getProjectContractors().add(this);
    }

    public void addApplication(Application application) {
        this.application = application;
        application.getProjectContractors().add(this);
    }

    private static ModelMapper modelMapper = new ModelMapper();

    public static ProjectContractor to(ProjectContractorDto projectContractorDto) {
        return modelMapper.map(projectContractorDto, ProjectContractor.class);
    }

}
