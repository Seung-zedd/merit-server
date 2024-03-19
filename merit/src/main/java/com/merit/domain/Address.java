package com.merit.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Address {

    @NotBlank(message = "Entering address is required")
    private String city;
    @NotBlank(message = "Entering address is required")
    private String street;
    @NotBlank(message = "Entering address is required")
    private String zipcode;
}
