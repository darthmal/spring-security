package com.salam.spring_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

@SpringBootApplication
public class SpringSecurityApplication {
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		System.out.println("Hello world");
//		KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
//		generator.initialize(new ECGenParameterSpec("secp256r1")); // Use a standard curve
//		KeyPair keyPair = generator.generateKeyPair();
//
//		// Access the PrivateKey
//		PrivateKey privateKey = keyPair.getPrivate();
//		String encodedKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
////		System.out.println(encodedKey);
//		if (privateKey instanceof PrivateKey) {
//			System.out.println("The key is an instance of PrivateKey");
//		} else {
//			System.out.println("The key is not an instance of PrivateKey");
//		}
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

}
