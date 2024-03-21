package com.merit.dto;

import lombok.*;
import com.merit.domain.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContractorDto {

    private Long id;
    private String name;
    private String contractorEmail;
    private String website;
    private ContractorStatus status;
    private Address address;
    private String contactNumber;
    private int experience;
    private int expectedPay;
    private String expectedPayCurrency;
    private PdfDocument resume;
    private Image avatar;

    private LocalDate createdOn;
    private LocalDate modifiedOn;

}