package com.salam.spring_security;

import com.salam.spring_security.models.user.Role;
import com.salam.spring_security.models.user.User;
import com.salam.spring_security.repository.UserRepository;
import com.salam.spring_security.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.*;

@SpringBootApplication
public class SpringSecurityApplication {
	public static void main(String[] args)  {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

//	Creating a default admin account if not exist
	@Bean
	CommandLineRunner run(UserService userService, UserRepository userRespo, PasswordEncoder passwordEncoder) { // Inject UserService
		return args -> {

			// Default admin user creation:
			if (userService.finUserByUsername("admin").isEmpty()) {
				User adminUser = new User();
				adminUser.setFirstname("Admin");
				adminUser.setLastName("Admin");
				adminUser.setEmail("admin@example.com");
				adminUser.setUsername("admin");
				adminUser.setPassword(passwordEncoder.encode("admin")); // In a real app, hash the password!
				adminUser.setRole(Role.ADMIN);

				userRespo.save(adminUser);
				System.out.println("Default admin user created!");
			}
		};
	}
}
