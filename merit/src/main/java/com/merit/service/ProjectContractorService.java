package com.merit.service;

import com.merit.domain.*;
import com.merit.dto.ProjectContractorDto;
import com.merit.mapper.ProjectContractorMapper;
import com.merit.repository.ContractorRepository;
import com.merit.repository.ProjectContractorRepository;
import com.merit.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectContractorService {

    private final ProjectContractorRepository projectContractorRepository;
    private final ProjectRepository projectRepository;
    private final ContractorRepository contractorRepository;

    // * (create) Contractor and Project entity is already created
    @Transactional
    public void createProjectContractor(ProjectContractorDto projectContractorDto, Long projectId, Long contractorId) {

        // create PC entity
        ProjectContractor projectContractor = ProjectContractorMapper.INSTANCE.to(projectContractorDto);
        projectContractor.setStatus(ProjectContractorStatus.PENDING);
        ProjectContractor savedProjectContractor = projectContractorRepository.save(projectContractor);


        Project findProject = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

        Contractor findContractor = contractorRepository.findById(contractorId).orElseThrow(() -> new EntityNotFoundException("Contractor not found with id: " + contractorId));

        // Project-Contractor
        savedProjectContractor.addProject(findProject);
        savedProjectContractor.addContractor(findContractor);

    }

    // * (Read)
    public ProjectContractorDto getProjectContractor(Long id) {
        ProjectContractor projectContractor = projectContractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ProjectContractor not found with id: " + id));
        return ProjectContractorMapper.INSTANCE.from(projectContractor);
    }

    // * (ReadAll)
    public List<ProjectContractorDto> getAllProjectContractors() {
        List<ProjectContractor> projectContractors = projectContractorRepository.findAll();
        return projectContractors.stream()
                .map(ProjectContractorMapper.INSTANCE::from)
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
                .build();
        ProjectContractor saved = projectContractorRepository.save(updatedProjectContractor);
        return saved.getId();
    }

    @Transactional
    public void approved(Long id) {
        ProjectContractor findProjectContractor = projectContractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ProjectContractor not found with id: " + id));
        findProjectContractor.setStatus(ProjectContractorStatus.ACCEPTED);
        findProjectContractor.getContractor().setStatus(ContractorStatus.ENGAGED);
    }

    @Transactional
    public void rejected(Long id) {
        ProjectContractor findProjectContractor = projectContractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ProjectContractor not found with id: " + id));

        findProjectContractor.setStatus(ProjectContractorStatus.REJECTED);
        findProjectContractor.getProject().setStatus(ProjectStatus.ARCHIVED);
    }

    // * (Delete)
    @Transactional
    public void deleteProjectContractor(Long id) {
        ProjectContractor findProjectContractor = projectContractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ProjectContractor not found with id: " + id));
        Contractor contractor = findProjectContractor.getContractor();
        Project project = findProjectContractor.getProject();

        findProjectContractor.removeContractor(contractor);
        findProjectContractor.removeProject(project);

        projectContractorRepository.deleteById(id);
    }
}
