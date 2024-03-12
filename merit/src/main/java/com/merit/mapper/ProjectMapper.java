package com.merit.mapper;

import lombok.RequiredArgsConstructor;
import com.merit.domain.Project;
import com.merit.dto.ProjectDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectMapper {
    private final ModelMapper modelMapper;

    public Project to(ProjectDto projectDto) {
        return modelMapper.map(projectDto, Project.class);
    }

    public ProjectDto from(Project project) {
        return modelMapper.map(project, ProjectDto.class);
    }
}
