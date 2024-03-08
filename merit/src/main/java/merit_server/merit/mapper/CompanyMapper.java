package merit_server.merit.mapper;

import lombok.RequiredArgsConstructor;
import merit_server.merit.domain.Company;
import merit_server.merit.dto.CompanyDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompanyMapper {
    private final ModelMapper modelMapper;

    public Company to(CompanyDto companyDto) {
        return modelMapper.map(companyDto, Company.class);
    }

    // convert Entity to Dto
    public CompanyDto from(Company company) {
        return modelMapper.map(company, CompanyDto.class);
    }
}
