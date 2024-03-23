package com.merit.dto;


import lombok.*;

import com.merit.domain.enums.ProjectStatus;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private Long id;
    private String name;
    private String projectDescription;

    private String role;
    private int minExpReqd;
    private int maxExpReqd;
    private int salaryRange;

    private ProjectStatus status;
    private String createdBy;
    private Long companyId;

    private LocalDate createdOn;
    private LocalDate modifiedOn;
}
