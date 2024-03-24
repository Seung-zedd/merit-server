package com.merit.service;

import com.merit.domain.*;
import com.merit.domain.embedded.Address;
import com.merit.domain.embedded.Image;
import com.merit.domain.embedded.PdfDocument;
import com.merit.domain.enums.ContractorStatus;
import com.merit.repository.*;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.merit.dto.ContractorDto;
import com.merit.mapper.ContractorMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@GraphQLApi
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContractorService {
    private final CompanyRepository companyRepository;
    private final ContractorRepository contractorRepository;

    // * (Create)Employer should be able to create an account for freelancers
    //! this should be called after accepting invitation email, that is, if the CompanyStatus set onto ACCEPTED
    // 이미 ProjectContractorService에서 연관관계 매핑하였음
    @Transactional
    @GraphQLMutation(name = "createContractor")
    //? graphQL은 반환된 문자열을 그대로 전달하므로, 별도의 ResponseEntity 상태코드가 필요없음
    public Long createContractor(ContractorDto contractorDto, Long companyId) {

        // create Contractor entity
        Contractor contractor = ContractorMapper.INSTANCE.to(contractorDto);

        contractor.setStatus(ContractorStatus.AVAILABLE);

        Contractor savedContractor = contractorRepository.save(contractor);

        // Contractor-Company, add Contractor account after confirming Verification mail
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));
        savedContractor.addCompany(company);

        return savedContractor.getId();
    }

    // * (Read)
    @GraphQLQuery(name = "getContractor")
    public ContractorDto getContractor(Long id) {
        Contractor contractor = contractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contractor not found with id: " + id));
        return ContractorMapper.INSTANCE.from(contractor);
    }

    // * (Read) should be able to view contractor listing
    @GraphQLQuery(name = "getAllContractors")
    public List<ContractorDto> getAllContractors() {
        List<Contractor> contractors = contractorRepository.findAll();
        return contractors.stream()
                .map(ContractorMapper.INSTANCE::from)
                .toList();
    }

    // * (Read-Condition) Employer should be able to view complete Contractor list available on the system.
    @GraphQLQuery(name = "getAvailableContractors")
    public List<ContractorDto> getAvailableContractors() {
        List<Contractor> allByStatus = contractorRepository.findAllByStatus(ContractorStatus.AVAILABLE);
        return allByStatus.stream()
                .map(ContractorMapper.INSTANCE::from)
                .toList();
    }

    // * (Update)
    //! ContractorDto에 맞게 Contractor만 수정하자
    @Transactional
    @GraphQLMutation(name = "updateContractor")
    public Long updateContractor(Long id, ContractorDto contractorDto) {
        Contractor contractor = contractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contractor not found with id: " + id));

        Address updatedAddress = Address.builder()
                .city(contractorDto.getAddress().getCity())
                .street(contractorDto.getAddress().getStreet())
                .zipcode(contractorDto.getAddress().getZipcode())
                .build();

        Contractor updatedContractor = contractor.toBuilder()
                .name(contractorDto.getName())
                .email(contractorDto.getEmail())
                .website(contractorDto.getWebsite())
                .status(contractorDto.getStatus())
                .address(updatedAddress)
                .avatar(new Image(contractorDto.getAvatar().getFileName()))
                .resume(new PdfDocument(contractorDto.getResume().getPdfFileName()))
                .contactNumber(contractorDto.getContactNumber())
                .expectedPay(contractorDto.getExpectedPay())
                .expectedPayCurrency(contractorDto.getExpectedPayCurrency())
                .build();

        Contractor savedContractor = contractorRepository.save(updatedContractor);
        log.debug("savedContractor={}", savedContractor);
        return savedContractor.getId();

    }

    // * (Delete)
    @Transactional
    @GraphQLMutation(name = "deleteContractor")
    public void deleteContractor(Long id, Long companyId) {
        Contractor contractor = contractorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Contractor not found with id: " + id));


        Company company = companyRepository.findById(companyId).orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));
        contractor.removeCompany(company);

        contractorRepository.deleteById(id);
    }
}
