package com.merit.dto;

import lombok.*;
import com.merit.domain.CompanyContractorStatus;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyContractorDto {

    private CompanyContractorStatus status;

    private LocalDate createdOn;
    private LocalDate modifiedOn;
}