package com.merit.service;

import com.merit.domain.*;
import com.merit.dto.ContractorDto;
import com.merit.dto.SkillDto;
import com.merit.mapper.SkillMapper;
import com.merit.openCsv.OpenCsv;
import com.merit.repository.ContractorRepository;
import com.merit.repository.SkillRepository;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
@Transactional
@Slf4j
class ContractorServiceTest {

    @Autowired
    private ContractorService contractorService;
    @Autowired
    private ContractorRepository contractorRepository;
    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillMapper skillMapper;

    @Test
    @DisplayName("A contractor must be created and saved in the DB.")
    void create() throws Exception, CsvValidationException {
        //given
        List<SkillDto> csvSkillDtos = OpenCsv.readSkillDataFromCsv("src/test/resources/skills.csv", 6);
        List<ContractorDto> csvContractorDtos = OpenCsv.readContractorDataFromCsv("src/test/resources/contractor.csv", 3);

        // when
        for (ContractorDto csvContractorDto : csvContractorDtos) {
            Long savedContractorId = contractorService.create(csvContractorDto, csvSkillDtos);

            //then
            assertThat(savedContractorId).isEqualTo(csvContractorDto.getId());
            log.debug("csvContractorDto={}", csvContractorDto);
            Optional<Contractor> optionalContractor = contractorRepository.findById(savedContractorId);
            Contractor savedContractor = optionalContractor.get();
            optionalContractor.ifPresent(contractor -> log.debug("Contractor={}", contractor));

        }
    }

    @Test
    @DisplayName("should read a certain Contractor in detail")
    void read() throws Exception {
        //given
        List<SkillDto> csvSkillDtos = OpenCsv.readSkillDataFromCsv("src/test/resources/skills.csv", 6);
        List<ContractorDto> csvContractorDtos = OpenCsv.readContractorDataFromCsv("src/test/resources/contractor.csv", 3);

        List<Contractor> savedContractors = new ArrayList<>();


        for (ContractorDto csvContractorDto : csvContractorDtos) {
            Long savedContractorId = contractorService.create(csvContractorDto, csvSkillDtos);

            //when
            Contractor savedContractor = contractorRepository.findById(savedContractorId).orElse(null);
            if (savedContractor != null) {
                savedContractors.add(savedContractor);
            }
        }

        //then
        ContractorDto contractorDto = contractorService.getContractor(savedContractors.get(1).getId());

        //then
        assertThat(contractorDto).isInstanceOf(ContractorDto.class);
        log.debug("contractorDto={}", contractorDto);
    }

    @Test
    @DisplayName("should read Contractor list")
    void readAll() throws Exception {
        //given
        List<SkillDto> csvSkillDtos = OpenCsv.readSkillDataFromCsv("src/test/resources/skills.csv", 6);
        List<ContractorDto> csvContractorDtos = OpenCsv.readContractorDataFromCsv("src/test/resources/contractor.csv", 3);

        for (ContractorDto csvContractorDto : csvContractorDtos) {
            contractorService.create(csvContractorDto, csvSkillDtos);
        }

        //when
        List<ContractorDto> contractorDtos = contractorService.getAllContractors();

        //then
        assertThat(contractorDtos).isInstanceOf(List.class);
        log.debug("contractorDtos={}", contractorDtos);
    }

    @Test
    @DisplayName("AVAILABLE 상태의 프리랜서를 조회할 수 있어야 한다.")
    void readAvailable() throws Exception {
        //given
        List<SkillDto> csvSkillDtos = OpenCsv.readSkillDataFromCsv("src/test/resources/skills.csv", 6);
        List<ContractorDto> csvContractorDtos = OpenCsv.readContractorDataFromCsv("src/test/resources/contractor.csv", 3);

        List<Contractor> savedContractors = new ArrayList<>();


        for (ContractorDto csvContractorDto : csvContractorDtos) {
            Long savedContractorId = contractorService.create(csvContractorDto, csvSkillDtos);

            //when
            Contractor savedContractor = contractorRepository.findById(savedContractorId).orElse(null);
            if (savedContractor != null) {
                savedContractors.add(savedContractor);
            }
        }

        //when
        List<ContractorDto> availableContractors = contractorService.getAvailableContractors();

        // then
        assertThat(availableContractors)
                .extracting(ContractorDto::getStatus)
                .allMatch(status -> status == ContractorStatus.AVAILABLE);
        log.debug("availableContractors={}", availableContractors);
    }

    @Test
    @DisplayName("프리랜서의 정보 및 스킬들도 업데이트되어야 한다.")
    void update() throws Exception {

        // given
        List<SkillDto> csvSkillDtos = OpenCsv.readSkillDataFromCsv("src/test/resources/skills.csv", 6);
        List<ContractorDto> csvContractorDtos = OpenCsv.readContractorDataFromCsv("src/test/resources/contractor.csv", 5);

        for (ContractorDto csvContractorDto : csvContractorDtos) {
            contractorService.create(csvContractorDto, csvSkillDtos);
        }

        ContractorDto contractorDto = ContractorDto.builder()
                .id(3L)
                .name("Mr.example")
                .avatar(new Image("example.jpg"))
                .resume(new PdfDocument("example345.pdf"))
                .address(new Address("Example city", "200 exam", "656-789"))
                .expectedPayCurrency("KRW")
                .contactNumber("123-4567")
                .email("example@gmail.com")
                .build();

        Skill newskill = Skill.builder()
                .id(1L)
                .name("exampleSKill")
                .skillsDescription("examplify.")
                .build();
        skillRepository.save(newskill);

        List<SkillDto> skillDtoToAdd = new ArrayList<>();

        List<Skill> skills = skillRepository.findAll();
        for (Skill skill : skills) {
            skillDtoToAdd.add(skillMapper.from(skill));
        }

        //when
        Long updatedId = contractorService.updateContractor(csvContractorDtos.get(0).getId(), contractorDto, skillDtoToAdd);

        //then
        log.debug("csvContractorDto={}", csvContractorDtos.get(2));
    }

    @Test
    @Transactional
    @DisplayName("프리랜서 계정이 삭제되어야 하고, 관련된 ContractorSkill까지 사라져야한다.")
    void delete() throws Exception {

        //given
        List<SkillDto> csvSkillDtos = OpenCsv.readSkillDataFromCsv("src/test/resources/skills.csv", 6);
        List<ContractorDto> csvContractorDtos = OpenCsv.readContractorDataFromCsv("src/test/resources/contractor.csv", 6);

        List<Contractor> savedContractors = new ArrayList<>();

        for (ContractorDto csvContractorDto : csvContractorDtos) {
            Long savedContractorId = contractorService.create(csvContractorDto, csvSkillDtos);

            Contractor savedContractor = contractorRepository.findById(savedContractorId).orElse(null);
            if (savedContractor != null) {
                savedContractors.add(savedContractor);
            }
        }

        //when
        contractorService.deleteContractor(savedContractors.get(0).getId());

        //then
        Optional<Contractor> deletedContractor = contractorRepository.findById(savedContractors.get(2).getId());
        assertThat(deletedContractor).isEmpty();
    }


}