package com.salam.spring_security.services;


import com.salam.spring_security.config.JwtService;
import com.salam.spring_security.controller.auth.AuthenticationRequest;
import com.salam.spring_security.controller.auth.AuthenticationResponse;
import com.salam.spring_security.controller.auth.RegisterRequest;
import com.salam.spring_security.models.cart.Cart;
import com.salam.spring_security.models.user.Role;
import com.salam.spring_security.models.user.User;
import com.salam.spring_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        System.out.println("Hello----------------");
        System.out.println(user);
        // Manually Create and Associate Cart
        Cart cart = new Cart(user);
        user.setCart(cart); // Associate cart with the user
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
