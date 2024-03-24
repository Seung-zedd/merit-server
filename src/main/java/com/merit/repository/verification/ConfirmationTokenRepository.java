package com.merit.repository.verification;

import com.merit.domain.verification.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, Long> {
    ConfirmationTokenEntity findByConfirmationToken(String confirmationToken);
}
