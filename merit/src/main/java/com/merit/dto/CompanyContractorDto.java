package com.merit.dto;

import com.merit.domain.BaseEntity;
import lombok.*;
import com.merit.domain.CompanyContractorStatus;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyContractorDto extends BaseEntity{

    private CompanyContractorStatus status;
}