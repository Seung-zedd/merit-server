package com.merit.mapper;

import com.merit.domain.Project;
import com.merit.dto.ProjectDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    ProjectDto from(Project project);

    Project to(ProjectDto projectDto);
}
