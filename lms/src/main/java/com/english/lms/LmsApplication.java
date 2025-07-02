package com.english.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LmsApplication {

    private final PasswordEncoder passwordEncoder;

    LmsApplication(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class, args);
		
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	    String encoded = encoder.encode("1234");
	    System.out.println("암호화된 비밀번호: " + encoded);
	}

}
