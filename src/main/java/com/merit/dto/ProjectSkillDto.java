package com.merit.dto;

import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.Data;

@Data
@GraphQLType
public class ProjectSkillDto {
    private boolean required;
}
