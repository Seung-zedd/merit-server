package com.merit.dto;

import lombok.*;
import com.merit.domain.ProjectContractorStatus;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectContractorDto {

    private ProjectContractorStatus status;
    private String comment;
    private String rateType;
    private float expectedRate;
    private int expectedHoursPerWeek;
    private String expectedPayCurrency;
    private LocalDate applicationDate;

}