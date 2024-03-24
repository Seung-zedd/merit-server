package com.merit.domain.verification;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"confirmationToken", "createdDate"})
@Getter
@Setter
public class ConfirmationTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long tokenId;
    private String confirmationToken;
    private LocalDateTime createdDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "USER_ID")
    private User user;

    public ConfirmationTokenEntity(User user) {
        this.user = user;
        confirmationToken = UUID.randomUUID().toString();
        createdDate = LocalDateTime.now();
    }

}
