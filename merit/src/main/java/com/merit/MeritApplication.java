package com.merit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MeritApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeritApplication.class, args);
	}
}
