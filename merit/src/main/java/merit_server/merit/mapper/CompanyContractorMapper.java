package merit_server.merit.mapper;

import lombok.RequiredArgsConstructor;
import merit_server.merit.domain.CompanyContractor;
import merit_server.merit.dto.CompanyContractorDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompanyContractorMapper {
    private final ModelMapper modelMapper;

    public CompanyContractor to(CompanyContractorDto companyContractorDto) {
        return modelMapper.map(companyContractorDto, CompanyContractor.class);
    }

    // convert Entity to Dto
    public CompanyContractorDto from(CompanyContractor companyContractor) {
        return modelMapper.map(companyContractor, CompanyContractorDto.class);
    }
}
