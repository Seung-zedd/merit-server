package merit_server.merit.dto;

import lombok.*;
import merit_server.merit.domain.Address;
import merit_server.merit.domain.Company;
import merit_server.merit.domain.CompanyStatus;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;


@Getter
@Setter
public class CompanyDto  {
    private Long id;
    private String name;
    private String email;
    private String website;
    private String contactNumber;
    private Address address;
    private String about;
    private CompanyStatus status;
    private LocalDateTime createdOn;


}