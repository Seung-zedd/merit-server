package com.merit.resolver;

import com.merit.domain.Contractor;
import com.merit.dto.ContractorDto;
import com.merit.service.ContractorService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractorResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final ContractorService contractorService;

    public ContractorDto registerContractor(ContractorDto contractorDto) {
        return contractorService.createContractor(contractorDto);
    }

    public ResponseEntity<?> confirmContractorAccount(String confirmationToken) {
        return contractorService.confirmEmail(confirmationToken);
    }
}
