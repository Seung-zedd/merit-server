package merit_server.merit.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPLICATION_ID")
    private Long id;

    @OneToMany(mappedBy = "application")
    private List<ProjectContractor> projectContractors = new ArrayList<>();

    @OneToOne(mappedBy = "application")
    @Setter(AccessLevel.NONE)
    private Contractor contractor;
}
