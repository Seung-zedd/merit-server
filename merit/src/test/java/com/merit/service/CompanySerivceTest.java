package com.merit.service;

import com.merit.domain.Address;
import com.merit.domain.Company;
import com.merit.domain.CompanyStatus;
import com.merit.dto.CompanyDto;
import com.merit.repository.CompanyRepository;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@Rollback(false)
@Transactional
@Slf4j
class CompanyServiceTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    @Test
    @DisplayName("An employer account must be created and saved in the DB.")
    void create() throws Exception
    {
        //given
        CompanyDto companyDto = CompanyDto.builder()
                .id(1L)
                .name("EcoSolutions Ltd")
                .email("contact@ecosolutionsltd.com")
                .website("www.ecosolutionsltd.com")
                .contactNumber("123-4567")
                .address(new Address("Innovation City", "200 Tech Park", "123-456"))
                .about("Dedicated to sustainable environmental innovations and green technology.")
                .status(CompanyStatus.ACTIVE)
                .build();

        //when
        Long companyId = companyService.createCompany(companyDto);

        //then
        assertThat(companyId).isEqualTo(1L);
        log.debug("CompanyDto={}", companyDto);
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        Company company = optionalCompany.get();
        optionalCompany.ifPresent(c -> log.debug("Company={}", company));
    }

    @Test
    @DisplayName("should read certain Company in detail")
    void read() throws Exception
    {
        //given
        Company company = Company.builder()
                .id(1L)
                .name("EcoSolutions Ltd")
                .email("contact@ecosolutionsltd.com")
                .website("www.ecosolutionsltd.com")
                .contactNumber("123-4567")
                .address(new Address("Innovation City", "200 Tech Park", "123-456"))
                .about("Dedicated to sustainable environmental innovations and green technology.")
                .status(CompanyStatus.ACTIVE)
                .build();
        Company savedCompany = companyRepository.save(company);

        //when
        CompanyDto companyDto = companyService.getCompany(savedCompany.getId());

        //then
        assertThat(companyDto).isInstanceOf(CompanyDto.class);
        log.debug("companyDto={}", companyDto);
    }

    @Test
    @DisplayName("should read Company list")
    void readAll() throws Exception
    {
        //given
        Company company1 = Company.builder()
                .id(1L)
                .name("EcoSolutions Ltd")
                .email("contact@ecosolutionsltd.com")
                .website("www.ecosolutionsltd.com")
                .contactNumber("123-4567")
                .address(new Address("Innovation City", "200 Tech Park", "123-456"))
                .about("Dedicated to sustainable environmental innovations and green technology.")
                .status(CompanyStatus.ACTIVE)
                .build();
        Company company2 = Company.builder()
                .id(5L)
                .name("CcoSolutions Ltd")
                .email("second@ecosolutionsltd.com")
                .website("www.Ccosolutionsltd.com")
                .contactNumber("223-4567")
                .address(new Address("Cnnovation City", "222 Tech Park", "222-222"))
                .about("Cedicated to sustainable environmental innovations and green technology.")
                .status(CompanyStatus.INACTIVE)
                .build();
        List<Company> companyList = List.of(company1, company2);
        companyRepository.saveAll(companyList);

        //when
        List<CompanyDto> companyDtos = companyService.getAllCompanies();

        //then
        assertThat(companyDtos).isInstanceOf(List.class);
        log.debug("companyDtos={}", companyDtos);
    }

    @Test
    @DisplayName("Company should be updated")
    void update() throws Exception
    {
        //given
        Company company = Company.builder()
                .id(1L)
                .name("EcoSolutions Ltd")
                .email("contact@ecosolutionsltd.com")
                .website("www.ecosolutionsltd.com")
                .contactNumber("123-4567")
                .address(new Address("Innovation City", "200 Tech Park", "123-456"))
                .about("Dedicated to sustainable environmental innovations and green technology.")
                .status(CompanyStatus.ACTIVE)
                .build();

        Company savedCompany = companyRepository.save(company);


        //when
        CompanyDto companyDto = CompanyDto.builder()
                .id(5L)
                .name("CcoSolutions Ltd")
                .email("second@ecosolutionsltd.com")
                .website("www.Ccosolutionsltd.com")
                .contactNumber("223-4567")
                .address(new Address("Cnnovation City", "222 Tech Park", "222-222"))
                .about("Cedicated to sustainable environmental innovations and green technology.")
                .status(CompanyStatus.INACTIVE)
                .build();

        Long updatedCompanyId = companyService.updateCompany(savedCompany.getId(), companyDto);
        Company findCompany = companyRepository.findById(updatedCompanyId).get();

        //then
        assertThat(findCompany.getName()).isEqualTo("CcoSolutions Ltd");
        log.debug("Company={}", company);
        log.debug("findCompany={}", findCompany);
    }

    @Test
    @DisplayName("should delete certain Company")
    void delete() throws Exception
    {
        //given
        Company company = Company.builder()
                .id(1L)
                .name("EcoSolutions Ltd")
                .email("contact@ecosolutionsltd.com")
                .website("www.ecosolutionsltd.com")
                .contactNumber("123-4567")
                .address(new Address("Innovation City", "200 Tech Park", "123-456"))
                .about("Dedicated to sustainable environmental innovations and green technology.")
                .status(CompanyStatus.ACTIVE)
                .build();

        Company findCompany = companyRepository.save(company);

        //when
        companyService.deleteCompany(findCompany.getId());

        //then
        Optional<Company> deletedCompany = companyRepository.findById(findCompany.getId());
        assertThat(deletedCompany).isEmpty();
    }
}