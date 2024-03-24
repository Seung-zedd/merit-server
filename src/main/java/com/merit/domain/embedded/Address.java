package com.merit.domain.embedded;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
