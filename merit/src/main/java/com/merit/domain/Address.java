package com.merit.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
