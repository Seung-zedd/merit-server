package com.merit.service.verification;

import com.merit.domain.verification.User;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> saveUser(User user);
    ResponseEntity<?> confirmEmail(String confirmationToken);
}
