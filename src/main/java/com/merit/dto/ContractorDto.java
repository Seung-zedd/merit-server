package com.merit.dto;

import com.merit.domain.embedded.Address;
import com.merit.domain.embedded.Image;
import com.merit.domain.embedded.PdfDocument;
import com.merit.domain.enums.ContractorStatus;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@GraphQLType
public class ContractorDto {

    private Long id;
    private String name;
    private String email;
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