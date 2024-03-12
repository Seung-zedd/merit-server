package com.merit.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class PdfDocument {

    @Column(nullable = false)
    private String pdfFileName;

    @Column(nullable = false)
    private String pdfFileOriName;

    @Column(nullable = false)
    private String pdfFileUrl;
}
