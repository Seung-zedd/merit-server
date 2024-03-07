package merit_server.merit.dto;

import lombok.Getter;
import lombok.Setter;
import merit_server.merit.domain.ProjectContractor;
import merit_server.merit.domain.ProjectContractorStatus;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ProjectContractorDto {

    private ProjectContractorStatus status;
    private String comment;
    private String rateType;
    private float expectedRate;
    private int expectedHoursPerWeek;
    private String expectedPayCurrency;
    private String applicationDate;

    private static ModelMapper modelMapper = new ModelMapper();

    // convert Entity to Dto
    public static ProjectContractorDto from(ProjectContractor projectContractor) {
        return modelMapper.map(projectContractor, ProjectContractorDto.class);
    }
}