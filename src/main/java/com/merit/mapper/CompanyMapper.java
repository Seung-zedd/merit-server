package com.merit.mapper;

import com.merit.domain.Company;
import com.merit.dto.CompanyDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyDto from(Company company);

    Company to(CompanyDto companyDto);


}
