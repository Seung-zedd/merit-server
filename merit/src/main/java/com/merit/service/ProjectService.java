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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
                    .required(new Random().nextBoolean())
                    .build();
            newProjectSkill.addProject(project);
            newProjectSkill.addSkill(skill);

            //? save()를 호출할 때 영속성 컨텍스트에 새로운 ProjectSkill 엔티티가 저장되고, 각각의 ProjectSkill 엔티티에 대해 고유한 식별자가 생성
            projectSkillRepository.save(newProjectSkill);
        }
        log.debug("projectSkillRepository.size={}", projectSkillRepository.findAll().size());

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
                .salaryRange(projectDto.getSalaryRange())
                .createdBy(projectDto.getCreatedBy())
                .build();

        Project savedProject = projectRepository.save(updatedProject);

        return savedProject.getId();
    }

    // * (Delete)
    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));

        List<ProjectSkill> projectSkillsToDelete = new ArrayList<>();

        // 프로젝트와 연관된 모든 프로젝트 스킬을 임시 컬렉션에 추가합니다.
        for (ProjectSkill projectSkill : new ArrayList<>(project.getProjectSkills())) {

            // 프로젝트와 연관된 스킬들만 제거한다
            Skill skill = projectSkill.getSkill();
            projectSkill.removeProject(project);
            projectSkill.removeSkill(skill);
            projectSkillsToDelete.add(projectSkill);

        }

        // 임시 컬렉션에 있는 프로젝트 스킬을 삭제합니다.
        projectSkillRepository.deleteAll(projectSkillsToDelete);

        // remove Project-Company relationship
        Company findCompany = project.getCompany();
        project.removeCompany(findCompany);
        log.debug("project.getCompany={}", project.getCompany());

        // 프로젝트를 삭제합니다.
        projectRepository.deleteById(id);
        // 프로젝트와 연관된 projectSkill도 실제로 삭제됬는지 log.debug()로 확인
        log.debug("ProjectSkills.size={}", project.getProjectSkills().size());
    }

    public void changeStatus(ProjectDto projectDto) {

    }

    private Project getProjectEntity(ProjectDto projectDto) {

        return Project.builder()
                .name(projectDto.getName())
                .projectDescription(projectDto.getProjectDescription())
                .role(projectDto.getRole())
                .minExpReqd(projectDto.getMinExpReqd())
                .maxExpReqd(projectDto.getMaxExpReqd())
                .status(ProjectStatus.OPEN)
                .createdBy(projectDto.getCreatedBy())
                .salaryRange(projectDto.getSalaryRange())
                .build();
    }
}