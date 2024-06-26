package com.salam.spring_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

@SpringBootApplication
public class SpringSecurityApplication {
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}
}
