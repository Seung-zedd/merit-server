package com.merit.dto;

import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.*;
import com.merit.domain.embedded.Address;
import com.merit.domain.enums.CompanyStatus;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@GraphQLType
public class CompanyDto  {
    private Long id;
    private String name;
    private String email;
    private String website;
    private String contactNumber;
    private Address address;
    private String about;
    private CompanyStatus status;

    private LocalDate createdOn;
    private LocalDate modifiedOn;

}