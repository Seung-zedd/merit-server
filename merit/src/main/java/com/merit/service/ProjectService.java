package com.merit.service;

import com.merit.dto.CompanyDto;
import com.merit.dto.ProjectSkillDto;
import com.merit.mapper.CompanyMapper;
import com.merit.repository.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.merit.domain.*;
import com.merit.dto.ProjectDto;
import com.merit.dto.SkillDto;
import com.merit.mapper.ProjectMapper;
import com.merit.mapper.SkillMapper;
import com.merit.repository.CompanyRepository;
import com.merit.repository.ProjectRepository;
import com.merit.repository.ProjectSkillRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;
    private final ProjectRepository projectRepository;
    private final ProjectSkillRepository projectSkillRepository;

    private final ProjectMapper projectMapper;
    private final SkillMapper skillMapper;
    private final CompanyMapper companyMapper;
    private ProjectSkillDto projectSkillDto;

    // * (Create)Employer should be able to create a new project
    // Return: projectId so that Employer can identify each project
    @Transactional
    public Long createProject(ProjectDto projectDto, List<SkillDto> skillDtos, Long companyId) {
        // create Project entity based on ProjectDto
        Project project = getProjectEntity(projectDto);

        // feat: Convert the Dto received from the user into a project entity and save it in the repository
        List<Skill> skills = skillDtos.stream()
                .map(skillMapper::to)
                .toList();

        skillRepository.saveAll(skills);
        Project savedProject = projectRepository.save(project);

        // Project-Skill
        // Lazy 초기화를 해줘야한다.(-> @Transactional로 해결)
        for (Skill skill : skills) {
            ProjectSkill newProjectSkill = ProjectSkill.builder()
                    .required(projectSkillDto.isRequired())
                    .build();
                newProjectSkill.addProject(project);
                newProjectSkill.addSkill(skill);
                projectSkillRepository.save(newProjectSkill);
        }

        // Project-Company
        Company findCompany = companyRepository.findById(companyId).orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));

        savedProject.addCompany(findCompany);
        return savedProject.getId();
    }

    // * (Read)Freelancer should be able to view their project details
    // 파라미터의 id는 Controller에서 @PathVariable로 받을 예정
    public ProjectDto getProject(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
        return projectMapper.from(project);
    }

    // * (Read)Employer should be able to view project listing
    // will be used in Controller
    public List<ProjectDto> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(projectMapper::from)
                .toList();
    }

    // * (Update)
    // Return: 테스트 환경에서 확인하기 위해서 Long 타입을 반환
    @Transactional
    public Long updateProject(Long id, ProjectDto projectDto) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));

        Project updatedProject = project.toBuilder()
                .name(projectDto.getName())
                .projectDescription(projectDto.getProjectDescription())
                .role(projectDto.getRole())
                .minExpReqd(projectDto.getMinExpReqd())
                .maxExpReqd(projectDto.getMaxExpReqd())
                .status(projectDto.getStatus())
                .createdBy(projectDto.getCreatedBy())
                .build();

        Project savedProject = projectRepository.save(updatedProject);

        return savedProject.getId();
    }

    // * (Delete)
    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    private Project getProjectEntity(ProjectDto projectDto) {

        return Project.builder()
                .name(projectDto.getName())
                .projectDescription(projectDto.getProjectDescription())
                .role(projectDto.getRole())
                .minExpReqd(projectDto.getMinExpReqd())
                .maxExpReqd(projectDto.getMaxExpReqd())
                .status(projectDto.getStatus())
                .createdBy(projectDto.getCreatedBy())
                .build();
    }
}