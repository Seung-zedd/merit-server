package com.merit.domain.verification;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;
    private String userName;
    private String password;
    private String userEmail;
    private boolean isEnabled;

    public User(String userName, String userEmail, String password, boolean isEnabled) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
        this.isEnabled = isEnabled;
    }

}
