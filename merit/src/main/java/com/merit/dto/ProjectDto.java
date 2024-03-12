package com.merit.dto;


import lombok.*;

import com.merit.domain.ProjectStatus;

import java.time.LocalDateTime;
import java.util.List;

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

    private boolean required;
}
