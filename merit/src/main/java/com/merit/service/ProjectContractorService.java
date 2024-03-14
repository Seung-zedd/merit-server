package com.merit.service;

import com.merit.domain.Contractor;
import com.merit.domain.Project;
import com.merit.domain.ProjectContractor;
import com.merit.dto.ContractorDto;
import com.merit.dto.ProjectContractorDto;
import com.merit.dto.ProjectDto;
import com.merit.mapper.ContractorMapper;
import com.merit.mapper.ProjectContractorMapper;
import com.merit.mapper.ProjectMapper;
import com.merit.repository.ProjectContractorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectContractorService {
    private final ProjectContractorRepository projectContractorRepository;
    private final ProjectContractorMapper projectContractorMapper;
    private final ProjectMapper projectMapper;
    private final ContractorMapper contractorMapper;

    @Transactional
    public Long createProjectContractor(ProjectContractorDto projectContractorDto, ProjectDto projectDto, ContractorDto contractorDto) {

        // create PC entity
        ProjectContractor projectContractor = projectContractorMapper.to(projectContractorDto);
        ProjectContractor savedPC = projectContractorRepository.save(projectContractor);

        Project project = projectMapper.to(projectDto);
        Contractor contractor = contractorMapper.to(contractorDto);

        // Project-Contractor
        savedPC.addProject(project);
        savedPC.addContractor(contractor);

        return savedPC.getId();
    }

    // * (Read)
    public ProjectContractorDto getProjectContractor(Long id) {
        ProjectContractor projectContractor = projectContractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ProjectContractor not found with id: " + id));
        return projectContractorMapper.from(projectContractor);
    }

    // * (ReadAll)
    public List<ProjectContractorDto> getAllProjectContractors() {
        List<ProjectContractor> projectContractors = projectContractorRepository.findAll();
        return projectContractors.stream()
                .map(projectContractorMapper::from)
                .toList();
    }

    // * (Update)
    @Transactional
    public Long updateProjectContractor(Long id, ProjectContractorDto projectContractorDto) {
        ProjectContractor projectContractor = projectContractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ProjectContractor not found with id: " + id));

        ProjectContractor updatedProjectContractor = projectContractor.toBuilder()
                .status(projectContractorDto.getStatus())
                .comment(projectContractorDto.getComment())
                .rateType(projectContractorDto.getRateType())
                .expectedRate(projectContractorDto.getExpectedRate())
                .expectedHoursPerWeek(projectContractorDto.getExpectedHoursPerWeek())
                .expectedPayCurrency(projectContractorDto.getExpectedPayCurrency())
                .applicationDate(LocalDate.now())
                .build();
        ProjectContractor saved = projectContractorRepository.save(updatedProjectContractor);
        return saved.getId();
    }

    // * (Delete)
    @Transactional
    public void deleteProjectContractor(Long id) {
        projectContractorRepository.deleteById(id);
    }
}
