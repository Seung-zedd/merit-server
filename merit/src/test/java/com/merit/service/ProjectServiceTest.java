package com.merit.service;

import com.merit.domain.Company;
import com.merit.domain.Project;
import com.merit.domain.ProjectSkill;
import com.merit.domain.ProjectStatus;
import com.merit.dto.ProjectDto;
import com.merit.dto.SkillDto;
import com.merit.mapper.ProjectMapper;
import com.merit.repository.CompanyRepository;
import com.merit.repository.ProjectRepository;
import com.merit.repository.ProjectSkillRepository;
import com.merit.repository.SkillRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
@Transactional
@Slf4j
class ProjectServiceTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Test
    @DisplayName("A project must be created and saved in the DB.")
    void create() throws Exception {
        //given
        // 먼저 projectDto, skillDto, 그리고 companyId가 필요하다
        ProjectDto projectDto = ProjectDto.builder()
                .id(1L)
                .name("AI Finance Predictor")
                .projectDescription("Developing a cutting-edge AI platform for predictive analytics in finance.")
                .role("AI Algorithm Engineer")
                .minExpReqd(3)
                .maxExpReqd(7)
                .status(ProjectStatus.OPEN)
                .createdBy("CMP001")
                .required(true)
                .build();

//        Long companyId = 1L;
        SkillDto skillDto1 = SkillDto.builder()
                .name("Python")
                .skillsDescription("research machine learning data")
                .id(1L)
                .build();

        SkillDto skillDto2 = SkillDto.builder()
                .id(2L)
                .name("Javascript")
                .skillsDescription("make interactive web using Javascript")
                .build();

        List<SkillDto> skillDtos = List.of(skillDto1, skillDto2);

        //when
        Long savedProjectId = projectService.createProject(projectDto, skillDtos);

        //then
        assertThat(savedProjectId).isEqualTo(projectDto.getId());
        log.debug("ProjectDto={}", projectDto);
        Optional<Project> optionalProject = projectRepository.findById(savedProjectId);
        Project savedProject = optionalProject.get();
        optionalProject.ifPresent(project -> log.debug("Project={}", project));
    }

    @Test
    @DisplayName("Project should be updated")
    void update() throws Exception {
        //given
        ProjectDto projectDto = ProjectDto.builder()
                .id(1L)
                .name("AI Finance Predictor")
                .projectDescription("Developing a cutting-edge AI platform for predictive analytics in finance.")
                .role("AI Algorithm Engineer")
                .minExpReqd(3)
                .maxExpReqd(7)
                .status(ProjectStatus.OPEN)
                .createdBy("CMP001")
                .required(true)
                .build();

        SkillDto skillDto1 = SkillDto.builder()
                .name("Python")
                .skillsDescription("research machine learning data")
                .id(1L)
                .build();

        SkillDto skillDto2 = SkillDto.builder()
                .id(2L)
                .name("Javascript")
                .skillsDescription("make interactive web using Javascript")
                .build();

        List<SkillDto> skillDtos = List.of(skillDto1, skillDto2);

        Long savedProjectId = projectService.createProject(projectDto, skillDtos);

        //when
        ProjectDto newProjectDto = ProjectDto.builder()
                .id(3L)
                .name("AI Predictor")
                .projectDescription("Developing a cutting-edge AI platform for predictive analytics in finance.")
                .role("AI Engineer")
                .minExpReqd(2)
                .maxExpReqd(5)
                .status(ProjectStatus.OPEN)
                .createdBy("CMP0011")
                .required(true)
                .build();

        Long updatedProjectId = projectService.updateProject(savedProjectId, newProjectDto);
        Project findProject = projectRepository.findById(updatedProjectId).get();

        //then
        assertThat(findProject.getName()).isEqualTo("AI Predictor");
        log.debug("ProjectDto={}", projectDto);
        log.debug("findProject={}", findProject);
    }

    @Test
    @DisplayName("should be read certain Project in detail")
    void readProject() throws Exception
    {
        //given
        Project project = Project.builder()
                .id(1L)
                .name("AI Finance Predictor")
                .projectDescription("Developing a cutting-edge AI platform for predictive analytics in finance.")
                .role("AI Algorithm Engineer")
                .minExpReqd(3)
                .maxExpReqd(7)
                .status(ProjectStatus.OPEN)
                .createdBy("CMP001")
                .required(true)
                .build();
        Project savedProject = projectRepository.save(project);

        //when
        ProjectDto projectDto = projectService.getProject(savedProject.getId());

        //then
        assertThat(projectDto).isInstanceOf(ProjectDto.class);
        log.debug("projectDto={}", projectDto);
    }

    @Test
    @DisplayName("should read Project list")
    void readAllProjects() throws Exception
    {
        //given
        Project project1 = Project.builder()
                .id(1L)
                .name("AI Finance Predictor")
                .projectDescription("Developing a cutting-edge AI platform for predictive analytics in finance.")
                .role("AI Algorithm Engineer")
                .minExpReqd(3)
                .maxExpReqd(7)
                .status(ProjectStatus.OPEN)
                .createdBy("CMP001")
                .required(true)
                .build();
        Project project2 = Project.builder()
                .id(2L)
                .name("BI Finance Predictor")
                .projectDescription("Developing a cutting-edge BI platform for predictive analytics in finance.")
                .role("BI Algorithm Engineer")
                .minExpReqd(1)
                .maxExpReqd(5)
                .status(ProjectStatus.OPEN)
                .createdBy("BMP001")
                .required(true)
                .build();
        List<Project> projectList = List.of(project1, project2);

        projectRepository.saveAll(projectList);

        //when
        List<ProjectDto> findProjectsDto = projectService.getAllProjects();

        //then
        assertThat(findProjectsDto).isInstanceOf(List.class);
        log.debug("findProjectsDto={}", findProjectsDto);
    }




    @Test
    @Transactional
    @DisplayName("should be delete certain Project")
    void deleteProject() throws Exception {
        //given
        Project project = Project.builder()
                .id(1L)
                .name("AI Finance Predictor")
                .projectDescription("Developing a cutting-edge AI platform for predictive analytics in finance.")
                .role("AI Algorithm Engineer")
                .minExpReqd(3)
                .maxExpReqd(7)
                .status(ProjectStatus.OPEN)
                .createdBy("CMP001")
                .required(true)
                .build();

        ProjectSkill projectSkill = new ProjectSkill();

        Project findProject = projectRepository.save(project);

        //when
        projectService.deleteProject(findProject.getId());

        //then
        Optional<Project> deletedProject = projectRepository.findById(findProject.getId());
        assertThat(deletedProject).isEmpty();
    }
}