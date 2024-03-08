package merit_server.merit.dto;

import lombok.Getter;
import lombok.Setter;
import merit_server.merit.domain.CompanyContractor;
import merit_server.merit.domain.CompanyContractorStatus;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;


@Getter
@Setter
public class CompanyContractorDto  {

    private LocalDateTime createdOn;
    private CompanyContractorStatus status;
    private LocalDateTime lastStatusDate;

}