package com.merit.dto;

import lombok.*;
import com.merit.domain.enums.ProjectContractorStatus;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectContractorDto {

    private Long id;
    private ProjectContractorStatus status;
    private String comment;
    private String rateType;
    private float expectedRate;
    private float expectedExchangeRate;
    private String expectedPayCurrency;
    private int expectedHoursPerWeek;
    private LocalDate applicationDate;

}