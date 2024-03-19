package com.merit.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
@Builder
@Getter
public class PdfDocument {

    // After creating an account, click the update button to upload
    private String pdfFileName;
}
