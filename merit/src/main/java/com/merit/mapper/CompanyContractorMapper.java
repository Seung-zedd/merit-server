package com.merit.mapper;

import com.merit.domain.CompanyContractor;
import com.merit.dto.CompanyContractorDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface CompanyContractorMapper {

    CompanyContractorMapper INSTANCE = Mappers.getMapper(CompanyContractorMapper.class);

    CompanyContractorDto from(CompanyContractor companyContractor);

    CompanyContractor to(CompanyContractorDto companyContractorDto);


}

