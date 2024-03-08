package merit_server.merit.dto;


import lombok.Getter;
import lombok.Setter;

import merit_server.merit.domain.Project;
import merit_server.merit.domain.ProjectStatus;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectDto {

    private Long id;
    private String name;
    private String projectDescription;

    private String role;
    private int minExpReqd;
    private int maxExpReqd;

    private ProjectStatus status;
    private String createdBy;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;

    private boolean required;


}
