package com.merit.service;

import com.merit.domain.*;
import com.merit.dto.CompanyDto;
import com.merit.dto.ProjectDto;
import com.merit.dto.SkillDto;
import com.merit.mapper.CompanyMapper;
import com.merit.mapper.ProjectMapper;
import com.merit.openCsv.OpenCsv;
import com.merit.repository.CompanyRepository;
import com.merit.repository.ProjectRepository;
import com.merit.repository.ProjectSkillRepository;
import com.merit.repository.SkillRepository;
import com.opencsv.exceptions.CsvValidationException;
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

import java.time.LocalDate;
import java.util.ArrayList;
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
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectService projectService;

    @Test
    @DisplayName("A project must be created and saved in the DB.")
    void create() throws Exception, CsvValidationException {
        //given
        // 먼저 projectDto, skillDto, 그리고 companyId가 필요하다
        List<ProjectDto> csvProjectDtos = OpenCsv.readProjectDataFromCsv("src/test/resources/project.csv", 6);
        List<SkillDto> csvSkillDtos = OpenCsv.readSkillDataFromCsv("src/test/resources/skills.csv", 6);
        CompanyDto csvCompanyDto = OpenCsv.readCompanyDataFromCsv("src/test/resources/company.csv");

        Company company = companyMapper.to(csvCompanyDto);
        Company savedCompany = companyRepository.save(company);

        //when
        for (ProjectDto csvProjectDto : csvProjectDtos) {
            Long savedProjectId = projectService.createProject(csvProjectDto, csvSkillDtos, savedCompany.getId());

            //then
            assertThat(savedProjectId).isEqualTo(csvProjectDto.getId());
            log.debug("csvProjectDto={}", csvProjectDto);
            Optional<Project> optionalProject = projectRepository.findById(savedProjectId);
            optionalProject.get();
            optionalProject.ifPresent(project -> log.debug("Project={}", project));
        }
    }

    @Test
    @DisplayName("should read a certain Project in detail")
    void readProject() throws Exception {
        // Given
        // First, we need projectDto, skillDto, and companyId
        List<ProjectDto> csvProjectDtos = OpenCsv.readProjectDataFromCsv("src/test/resources/project.csv", 6);
        List<SkillDto> csvSkillDtos = OpenCsv.readSkillDataFromCsv("src/test/resources/skills.csv", 6);
        CompanyDto csvCompanyDto = OpenCsv.readCompanyDataFromCsv("src/test/resources/company.csv");

        Company company = companyMapper.to(csvCompanyDto);
        Company savedCompany = companyRepository.save(company);

        List<Project> savedProjects = new ArrayList<>();

        for (ProjectDto csvProjectDto : csvProjectDtos) {
            Long savedProjectId = projectService.createProject(csvProjectDto, csvSkillDtos, savedCompany.getId());
            Project savedProject = projectRepository.findById(savedProjectId).orElse(null);
            if (savedProject != null) {
                savedProjects.add(savedProject);
            }
        }

        // When
        ProjectDto projectDto = projectService.getProject(savedProjects.get(2).getId());

        // Then
        assertThat(projectDto).isInstanceOf(ProjectDto.class);
        log.debug("projectDto={}", projectDto);
    }

    @Test
    @DisplayName("should read Project list")
    void readAllProjects() throws Exception {
        // Given
        List<ProjectDto> csvProjectDtos = OpenCsv.readProjectDataFromCsv("src/test/resources/project.csv", 6);
        List<SkillDto> csvSkillDtos = OpenCsv.readSkillDataFromCsv("src/test/resources/skills.csv", 6);
        CompanyDto csvCompanyDto = OpenCsv.readCompanyDataFromCsv("src/test/resources/company.csv");

        Company company = companyMapper.to(csvCompanyDto);
        Company savedCompany = companyRepository.save(company);

        List<Project> savedProjects = new ArrayList<>();

        for (ProjectDto csvProjectDto : csvProjectDtos) {
            Long savedProjectId = projectService.createProject(csvProjectDto, csvSkillDtos, savedCompany.getId());
            Project savedProject = projectRepository.findById(savedProjectId).orElse(null);
            if (savedProject != null) {
                savedProjects.add(savedProject);
            }
        }

        // when
        List<ProjectDto> projectDtos = projectService.getAllProjects();

        // then
        assertThat(projectDtos).isInstanceOf(List.class);
        log.debug("projectDtos={}", projectDtos);
    }


    @Test
    @DisplayName("Project should be updated")
    void update() throws Exception {
        // Given
        List<ProjectDto> csvProjectDtos = OpenCsv.readProjectDataFromCsv("src/test/resources/project.csv", 6);
        List<SkillDto> csvSkillDtos = OpenCsv.readSkillDataFromCsv("src/test/resources/skills.csv", 6);
        CompanyDto csvCompanyDto = OpenCsv.readCompanyDataFromCsv("src/test/resources/company.csv");

        Company company = companyMapper.to(csvCompanyDto);
        Company savedCompany = companyRepository.save(company);

        List<Project> savedProjects = new ArrayList<>();

        for (ProjectDto csvProjectDto : csvProjectDtos) {
            Long savedProjectId = projectService.createProject(csvProjectDto, csvSkillDtos, savedCompany.getId());
            Project savedProject = projectRepository.findById(savedProjectId).orElse(null);
            if (savedProject != null) {
                savedProjects.add(savedProject);
            }
        }

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
                .createdOn(LocalDate.now())
                .modifiedOn(LocalDate.now())
                .build();
        Project newProject = projectMapper.to(newProjectDto);
        Project savedProject = projectRepository.save(newProject);

        Long updatedProjectId = projectService.updateProject(savedProject.getId(), newProjectDto);
        Project findProject = projectRepository.findById(updatedProjectId).get();

        //then
        assertThat(findProject.getName()).isEqualTo("AI Predictor");
        log.debug("csvProjectDtos={}", csvProjectDtos);
        log.debug("findProject={}", findProject);
    }

    @Test
    @Transactional
    @DisplayName("should be delete certain Project")
    void deleteProject() throws Exception {
        //given
        List<ProjectDto> csvProjectDtos = OpenCsv.readProjectDataFromCsv("src/test/resources/project.csv", 6);
        List<SkillDto> csvSkillDtos = OpenCsv.readSkillDataFromCsv("src/test/resources/skills.csv", 6);
        CompanyDto csvCompanyDto = OpenCsv.readCompanyDataFromCsv("src/test/resources/company.csv");

        Company company = companyMapper.to(csvCompanyDto);
        Company savedCompany = companyRepository.save(company);

        List<Project> savedProjects = new ArrayList<>();

        for (ProjectDto csvProjectDto : csvProjectDtos) {
            Long savedProjectId = projectService.createProject(csvProjectDto, csvSkillDtos, savedCompany.getId());

            Project savedProject = projectRepository.findById(savedProjectId).orElse(null);
            if (savedProject != null) {
                savedProjects.add(savedProject);
            }
        }

        //when
        projectService.deleteProject(savedProjects.get(0).getId());

        //then
        Optional<Project> deletedProject = projectRepository.findById(savedProjects.get(0).getId());
        assertThat(deletedProject).isEmpty();

    }
}