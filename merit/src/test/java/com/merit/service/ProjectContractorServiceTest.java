package com.merit.service;

import com.merit.domain.*;
import com.merit.dto.CompanyDto;
import com.merit.dto.ContractorDto;
import com.merit.dto.ProjectContractorDto;
import com.merit.dto.ProjectDto;
import com.merit.mapper.CompanyMapper;
import com.merit.mapper.ContractorMapper;
import com.merit.mapper.ProjectContractorMapper;
import com.merit.mapper.ProjectMapper;
import com.merit.openCsv.OpenCsv;
import com.merit.repository.CompanyRepository;
import com.merit.repository.ContractorRepository;
import com.merit.repository.ProjectContractorRepository;
import com.merit.repository.ProjectRepository;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@Rollback(false)
@Transactional
@Slf4j
class ProjectContractorServiceTest {

    @Autowired
    private ProjectContractorService projectContractorService;
    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    ProjectContractorRepository projectContractorRepository;

    @BeforeEach
    void init() throws IOException, CsvValidationException{
        //given
        List<Long> projectIds = new ArrayList<>();
        List<Long> contractorIds = new ArrayList<>();

        List<ProjectDto> csvProjectDtos = OpenCsv.readProjectDataFromCsv("src/test/resources/project.csv", 6);
        List<ContractorDto> csvContractorDtos = OpenCsv.readContractorDataFromCsv("src/test/resources/contractor.csv", 6);

        List<ProjectContractorDto> csvProjectContractorDtos = OpenCsv.readProjectContractorDataFromCsv("src/test/resources/project_contractor_bridge.csv", 6);

        for (ContractorDto csvContractorDto : csvContractorDtos) {
            Contractor contractor = ContractorMapper.INSTANCE.to(csvContractorDto);
            Contractor savedContractor = contractorRepository.saveAndFlush(contractor);

            contractorIds.add(savedContractor.getId());
        }

        for (ProjectDto csvProjectDto : csvProjectDtos) {
            Project project = ProjectMapper.INSTANCE.to(csvProjectDto);
            Project savedProject = projectRepository.saveAndFlush(project);

            projectIds.add(savedProject.getId());
        }

        for (int i = 0; i < csvProjectContractorDtos.size(); i++) {
            ProjectContractorDto csvProjectContractorDto = csvProjectContractorDtos.get(i);
            long projectId = projectIds.get(i);
            long contractorId = contractorIds.get(i);

            projectContractorService.create(csvProjectContractorDto, projectId, contractorId);
        }
    }

    @Test
    void create() throws Exception
    {
        //given
        List<Long> projectIds = new ArrayList<>();
        List<Long> contractorIds = new ArrayList<>();

        List<ProjectDto> csvProjectDtos = OpenCsv.readProjectDataFromCsv("src/test/resources/project.csv", 6);
        List<ContractorDto> csvContractorDtos = OpenCsv.readContractorDataFromCsv("src/test/resources/contractor.csv", 6);

        List<ProjectContractorDto> csvProjectContractorDtos = OpenCsv.readProjectContractorDataFromCsv("src/test/resources/project_contractor_bridge.csv", 6);

        for (ContractorDto csvContractorDto : csvContractorDtos) {
            Contractor contractor = ContractorMapper.INSTANCE.to(csvContractorDto);
            Contractor savedContractor = contractorRepository.saveAndFlush(contractor);

            contractorIds.add(savedContractor.getId());
        }



        for (ProjectDto csvProjectDto : csvProjectDtos) {
            Project project = ProjectMapper.INSTANCE.to(csvProjectDto);
            Project savedProject = projectRepository.saveAndFlush(project);

            projectIds.add(savedProject.getId());
        }

        //when
        for (int i = 0; i < csvProjectContractorDtos.size(); i++) {
            ProjectContractorDto csvProjectContractorDto = csvProjectContractorDtos.get(i);
            long projectId = projectIds.get(i);
            long contractorId = contractorIds.get(i);

            projectContractorService.create(csvProjectContractorDto, projectId, contractorId);
        }
    }

    @Test
    void read() throws Exception {
        //when
        ProjectContractorDto findProjectContractor = projectContractorService.getProjectContractor(3L);

        //then
        assertThat(findProjectContractor.getComment()).isEqualTo("Looking forward to working together");
    }

    @Test
    void readAll() throws Exception {
        //when
        List<ProjectContractorDto> allProjectContractors = projectContractorService.getAllProjectContractors();

        //then
        log.debug("allProjectContractors={}", allProjectContractors);
    }

    @Test
    void update() throws Exception {
        //given
        ProjectContractorDto projectContractorDto = ProjectContractorDto.builder()
                .id(3L)
                .applicationDate(LocalDate.of(1999, 3, 15))
                .expectedPayCurrency("KRW")
                .expectedHoursPerWeek(66)
                .expectedRate(1.11F)
                .status(ProjectContractorStatus.REJECTED)
                .rateType("monthly")
                .comment("testing...")
                .expectedExchangeRate(2.22F)
                .build();
        Long id = projectContractorRepository.findById(3L).get().getId();

        //when
        Long updatedId = projectContractorService.updateProjectContractor(id, projectContractorDto);
        ProjectContractor projectContractor = projectContractorRepository.findById(updatedId).get();

        //then
        assertThat(projectContractor.getComment()).isEqualTo("testing...");
    }

    @Test
    void delete() throws Exception {
        //when
        projectContractorService.deleteProjectContractor(2L);

        //then
        Optional<ProjectContractor> deletedPC = projectContractorRepository.findById(2L);
        assertThat(deletedPC).isEmpty();
    }

}