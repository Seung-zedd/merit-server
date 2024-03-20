package com.merit.mapper;

import com.merit.domain.Contractor;
import com.merit.dto.ContractorDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface ContractorMapper {

    ContractorMapper INSTANCE = Mappers.getMapper(ContractorMapper.class);

    ContractorDto from(Contractor contractor);

    Contractor to(ContractorDto contractorDto);

}

