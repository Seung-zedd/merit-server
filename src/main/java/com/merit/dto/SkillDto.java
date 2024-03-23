package com.merit.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillDto  {
    private Long id;
    private String name;
    private String skillsDescription;
}