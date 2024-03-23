package com.merit.mapper;

import com.merit.domain.bridge.ProjectContractor;
import com.merit.dto.ProjectContractorDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectContractorMapper {

    ProjectContractorMapper INSTANCE = Mappers.getMapper(ProjectContractorMapper.class);

    ProjectContractorDto from(ProjectContractor projectContractor);

    @Mapping(target = "applicationDate", expression = "java(java.time.LocalDate.now())")
    ProjectContractor to(ProjectContractorDto projectContractorDto);

}
