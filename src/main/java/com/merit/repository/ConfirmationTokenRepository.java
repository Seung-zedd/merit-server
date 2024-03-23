package com.merit.repository;

import com.merit.domain.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, Long> {
    ConfirmationTokenEntity findByConfirmationToken(String confirmationToken);
}
