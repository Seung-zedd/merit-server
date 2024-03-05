package merit_server.merit.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "APPLICATION")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPLICATION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private String comment;
    private LocalDateTime applicationDate;

    @OneToMany(mappedBy = "application", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<ContractorApplication> contractorApplications = new ArrayList<>();

    public void setProject(Project project) {
        this.project = project;
        project.getApplications().add(this);
    }
}
