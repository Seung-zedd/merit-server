package com.merit.dto;


import lombok.*;

import com.merit.domain.ProjectStatus;

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

    private ProjectStatus status;
    private String createdBy;
    private String companyId;

    private LocalDate createdOn;
    private LocalDate modifiedOn;
}
