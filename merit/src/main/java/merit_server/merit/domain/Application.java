package merit_server.merit.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "APPLICATION")
@Data
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

    @OneToMany(mappedBy = "application")
    private List<ContractorApplication> caList = new ArrayList<>();
    private String comment;
    private LocalDateTime applicationDate;
}
