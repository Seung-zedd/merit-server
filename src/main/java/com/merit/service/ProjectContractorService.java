package com.merit.service;

import com.merit.domain.*;
import com.merit.domain.bridge.ProjectContractor;
import com.merit.domain.enums.ContractorStatus;
import com.merit.domain.enums.ProjectContractorStatus;
import com.merit.domain.enums.ProjectStatus;
import com.merit.dto.ProjectContractorDto;
import com.merit.mapper.ProjectContractorMapper;
import com.merit.repository.ContractorRepository;
import com.merit.repository.ProjectContractorRepository;
import com.merit.repository.ProjectRepository;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@GraphQLApi
@RequiredArgsConstructor
public class ProjectContractorService {

    private final ProjectContractorRepository projectContractorRepository;
    private final ProjectRepository projectRepository;
    private final ContractorRepository contractorRepository;

    // * (create) Contractor and Project entity is already created
    @Transactional
    @GraphQLMutation(name = "createProjectContractor")
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
    @GraphQLQuery(name = "getProjectContractor")
    public ProjectContractorDto getProjectContractor(Long id) {
        ProjectContractor projectContractor = projectContractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ProjectContractor not found with id: " + id));
        return ProjectContractorMapper.INSTANCE.from(projectContractor);
    }

    // * (ReadAll)
    @GraphQLQuery(name = "getAllProjectContractors")
    public List<ProjectContractorDto> getAllProjectContractors() {
        List<ProjectContractor> projectContractors = projectContractorRepository.findAll();
        return projectContractors.stream()
                .map(ProjectContractorMapper.INSTANCE::from)
                .toList();
    }

    // * (Update)
    @Transactional
    @GraphQLMutation(name = "updateProjectContractor")
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
    @GraphQLMutation(name = "approved")
    public void approved(Long id) {
        ProjectContractor findProjectContractor = projectContractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ProjectContractor not found with id: " + id));
        findProjectContractor.setStatus(ProjectContractorStatus.ACCEPTED);
        findProjectContractor.getContractor().setStatus(ContractorStatus.ENGAGED);
    }

    @Transactional
    @GraphQLMutation(name = "rejected")
    public void rejected(Long id) {
        ProjectContractor findProjectContractor = projectContractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ProjectContractor not found with id: " + id));

        findProjectContractor.setStatus(ProjectContractorStatus.REJECTED);
        findProjectContractor.getProject().setStatus(ProjectStatus.ARCHIVED);
    }

    // * (Delete)
    @Transactional
    @GraphQLMutation(name = "deleteProjectContractor")
    public void deleteProjectContractor(Long id) {
        ProjectContractor findProjectContractor = projectContractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ProjectContractor not found with id: " + id));
        Contractor contractor = findProjectContractor.getContractor();
        Project project = findProjectContractor.getProject();

        findProjectContractor.removeContractor(contractor);
        findProjectContractor.removeProject(project);

        projectContractorRepository.deleteById(id);
    }
}
