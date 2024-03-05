package merit_server.merit.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ContractorApplication {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACTOR_ID")
    private Contractor contractor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPLICATION_ID")
    private Application application;

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
        contractor.getContractorApplications().add(this);
    }

    public void setApplication(Application application) {
        this.application = application;
        application.getContractorApplications().add(this);
    }

}
