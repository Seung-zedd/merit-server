package com.merit.service;

import com.merit.domain.embedded.Address;
import com.merit.domain.Company;
import com.merit.dto.CompanyDto;
import com.merit.mapper.CompanyMapper;
import com.merit.repository.CompanyRepository;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@GraphQLApi
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    // * (Create)Employer should be able to log in.
    @Transactional
    @GraphQLMutation(name = "createCompany")
    public Long createCompany(CompanyDto companyDto) {

        Company company = CompanyMapper.INSTANCE.to(companyDto);
        Company savedCompany = companyRepository.save(company);

        return savedCompany.getId();
    }

    // * (Read) should read Company's detail
    @GraphQLQuery(name = "getCompany")
    public CompanyDto getCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));
        return CompanyMapper.INSTANCE.from(company);
    }

    // * (Read) should be able to view company listing
    @GraphQLQuery(name = "getAllCompanies")
    public List<CompanyDto> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(CompanyMapper.INSTANCE::from)
                .toList();
    }

    // * (Update)CQS 원칙에 의해 업데이트된 정보는 뷰에서 리다이렉트시켜서 보여주면 됨
    @Transactional
    @GraphQLMutation(name = "updateCompany")
    public Long updateCompany(Long id, CompanyDto companyDto) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));

        Address updatedAddress = Address.builder()
                .city(companyDto.getAddress().getCity())
                .street(companyDto.getAddress().getStreet())
                .zipcode(companyDto.getAddress().getZipcode())
                .build();

        Company updatedCompany = company.toBuilder()
                .name(companyDto.getName())
                .email(companyDto.getEmail())
                .website(companyDto.getWebsite())
                .contactNumber(companyDto.getContactNumber())
                .address(updatedAddress)
                .about(companyDto.getAbout())
                .status(companyDto.getStatus())
                .build();

        Company savedCompany = companyRepository.save(updatedCompany);

        return savedCompany.getId();
    }

    // * (Delete)
    @Transactional
    @GraphQLMutation(name = "deleteCompany")
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}
