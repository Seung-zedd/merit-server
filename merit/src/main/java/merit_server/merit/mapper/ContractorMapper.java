package merit_server.merit.mapper;

import lombok.RequiredArgsConstructor;
import merit_server.merit.domain.Contractor;
import merit_server.merit.dto.ContractorDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractorMapper {
    private final ModelMapper modelMapper;

    public Contractor to(ContractorDto contractorDto) {
        return modelMapper.map(contractorDto, Contractor.class);
    }

    // convert Entity to Dto
    public ContractorDto from(Contractor contractor) {
        return modelMapper.map(contractor, ContractorDto.class);
    }
}
