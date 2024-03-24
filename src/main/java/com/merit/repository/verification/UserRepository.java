package com.merit.repository.verification;

import com.merit.domain.verification.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserEmailIgnoreCase(String emailId);

    Boolean existsByUserEmail(String email);
}
