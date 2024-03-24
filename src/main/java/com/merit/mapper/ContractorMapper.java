package com.merit.mapper;

import com.merit.domain.Contractor;
import com.merit.dto.ContractorDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContractorMapper {

    ContractorMapper INSTANCE = Mappers.getMapper(ContractorMapper.class);

    ContractorDto from(Contractor contractor);

    Contractor to(ContractorDto contractorDto);

}

