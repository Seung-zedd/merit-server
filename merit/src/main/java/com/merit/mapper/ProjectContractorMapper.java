package com.merit.mapper;

import lombok.RequiredArgsConstructor;
import com.merit.domain.ProjectContractor;
import com.merit.dto.ProjectContractorDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectContractorMapper {
    private final ModelMapper modelMapper;

    public ProjectContractor to(ProjectContractorDto projectContractorDto) {
        return modelMapper.map(projectContractorDto, ProjectContractor.class);
    }

    // convert Entity to Dto
    public ProjectContractorDto from(ProjectContractor projectContractor) {
        return modelMapper.map(projectContractor, ProjectContractorDto.class);
    }
}
