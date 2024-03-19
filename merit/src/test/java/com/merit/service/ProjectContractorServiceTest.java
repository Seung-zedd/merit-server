package com.merit.service;

import com.merit.domain.Company;
import com.merit.domain.Project;
import com.merit.dto.CompanyDto;
import com.merit.dto.ProjectContractorDto;
import com.merit.dto.ProjectDto;
import com.merit.mapper.CompanyMapper;
import com.merit.mapper.ProjectMapper;
import com.merit.openCsv.OpenCsv;
import com.merit.repository.CompanyRepository;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
@Transactional
@Slf4j
class ProjectContractorServiceTest {

    @Autowired
    private ProjectContractorService projectContractorService;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private CompanyMapper companyMapper;


    @Test
    void create() throws Exception
    {
        //given
        List<Long> projectIds = new ArrayList<>();
        List<ProjectContractorDto> csvProjectContractorDtos = OpenCsv.readProjectContractorDataFromCsv("src/test/resources/project_contractor_bridge.csv", 6);
        List<ProjectDto> csvProjectDtos = OpenCsv.readProjectDataFromCsv("src/test/resources/project.csv", 6);
        CompanyDto csvCompanyDto = OpenCsv.readCompanyDataFromCsv("src/test/resources/company.csv");

        Company company = companyMapper.to(csvCompanyDto);
        Company savedCompany = companyRepository.saveAndFlush(company);

        for (ProjectDto csvProjectDto : csvProjectDtos) {
            Project project = projectMapper.to(csvProjectDto);
            Project savedProject = projectRepository.saveAndFlush(project);

            projectIds.add(savedProject.getId());
        }

        //when
        for (ProjectContractorDto csvProjectContractorDto : csvProjectContractorDtos) {
            for (Long projectId : projectIds) {
                Long savedPcId = projectContractorService.create(csvProjectContractorDto, projectId, savedCompany.getId());

                //then
                assertThat(savedPcId).isEqualTo(csvProjectContractorDto.getId());
            }

        }





    }


}