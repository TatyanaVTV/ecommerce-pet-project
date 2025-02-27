package ru.petproject.ecommerce.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import ru.petproject.ecommerce.user_service.dto.JwtAuthenticationResponse;
import ru.petproject.ecommerce.user_service.dto.LoginRequest;
import ru.petproject.ecommerce.user_service.dto.SignUpRequest;
import ru.petproject.ecommerce.user_service.kafka.KafkaProducer;
import ru.petproject.ecommerce.user_service.model.User;
import ru.petproject.ecommerce.user_service.repository.UserRepository;
import ru.petproject.ecommerce.user_service.security.JwtTokenProvider;
import ru.petproject.ecommerce.user_service.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private KafkaProducer kafkaProducer;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            String jwt = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already taken!");
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setName(signUpRequest.getName());
        user.setRole(signUpRequest.getRole());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        kafkaProducer.sendMessage("User registered: " + user.getEmail());

        return ResponseEntity.ok("User registered successfully!");
    }

    @GetMapping("/isAdmin")
    public ResponseEntity<?> isUserAdmin() {
        // Получаем данные аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        // Проверяем, что principal является UserPrincipal
        if (!(authentication.getPrincipal() instanceof UserPrincipal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authentication");
        }

        // Получаем UserPrincipal из аутентификации
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Проверяем, является ли пользователь администратором
        boolean isAdmin = userPrincipal.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ADMIN"));

        return ResponseEntity.ok(isAdmin);
    }
}