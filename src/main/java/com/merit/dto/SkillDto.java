package com.merit.dto;

import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@GraphQLType
public class SkillDto  {

    private Long id;
    private String name;
    private String skillsDescription;
}