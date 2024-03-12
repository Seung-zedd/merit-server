package com.merit.dto;

import lombok.*;
import com.merit.domain.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

}