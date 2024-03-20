package com.merit.mapper;

import com.merit.domain.Company;
import com.merit.dto.CompanyDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyDto from(Company company);

    Company to(CompanyDto companyDto);


}
