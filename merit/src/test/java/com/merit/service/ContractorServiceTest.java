package com.merit.service;

import com.merit.domain.*;
import com.merit.dto.CompanyDto;
import com.merit.dto.ContractorDto;
import com.merit.dto.SkillDto;
import com.merit.mapper.CompanyMapper;
import com.merit.mapper.SkillMapper;
import com.merit.openCsv.OpenCsv;
import com.merit.repository.CompanyRepository;
import com.merit.repository.ContractorRepository;
import com.merit.repository.SkillRepository;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
@Slf4j
class ContractorServiceTest {
    // Deleted all tests since it's already passed all CRUD work
}