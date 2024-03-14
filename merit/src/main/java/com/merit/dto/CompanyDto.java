package com.merit.dto;

import lombok.*;
import com.merit.domain.Address;
import com.merit.domain.CompanyStatus;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
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