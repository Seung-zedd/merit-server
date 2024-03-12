package com.merit.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Address {

    @NotBlank(message = "주소 입력은 필수입니다")
    private String city;
    @NotBlank(message = "주소 입력은 필수입니다")
    private String street;
    @NotBlank(message = "주소 입력은 필수입니다")
    private String zipcode;
}
