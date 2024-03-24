package com.merit.service.verification;

import com.merit.domain.verification.ConfirmationTokenEntity;
import com.merit.domain.verification.User;
import com.merit.repository.verification.ConfirmationTokenRepository;
import com.merit.repository.verification.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public ResponseEntity<?> saveUser(User user) {

        if (Boolean.TRUE.equals(userRepository.existsByUserEmail(user.getUserEmail()))) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        userRepository.save(user);

        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(user);

        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getUserEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);

        log.debug("Confirmation Token={}", confirmationToken.getConfirmationToken());

        return ResponseEntity.ok("Verify email by the link sent on your email address");
    }

    @Override
    @Transactional
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationTokenEntity token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = userRepository.findByUserEmailIgnoreCase(token.getUser().getUserEmail());
            user.setEnabled(true);
            userRepository.save(user);
            log.debug("isEnabled={}", user.isEnabled());
            return ResponseEntity.ok("Email verified successfully!");
        }

        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }
}
