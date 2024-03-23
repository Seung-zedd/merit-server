package com.merit.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"confirmationToken", "createdDate"})
@Getter
public class ConfirmationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    private String confirmationToken;

    private LocalDateTime createdDate;

    @OneToOne(targetEntity = Contractor.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "CONTRACTOR_ID")
    private Contractor contractor;

    public ConfirmationTokenEntity(Contractor contractor) {
        this.contractor = contractor;
        this.confirmationToken = UUID.randomUUID().toString();
        createdDate = LocalDateTime.now();
    }

}
