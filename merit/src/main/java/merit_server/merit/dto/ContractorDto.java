package merit_server.merit.dto;

import lombok.Getter;
import lombok.Setter;
import merit_server.merit.domain.*;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
public class ContractorDto {

    private Long id;
    private String name;
    private String email;
    private String website;
    private ContractorStatus status;
    private Address address;
    private String contactNumber;
    private int experience;
    private int expectedPay;
    private String expectedPayCurrency;
    private PdfDocument resume;
    private Image avatar;
    private LocalDateTime createdOn;
    private LocalDateTime lastUpdatedOn;



}