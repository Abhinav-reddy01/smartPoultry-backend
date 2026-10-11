package com.poultry.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.poultry.entity.User;
import com.poultry.repository.UserRepository;
import com.poultry.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String identifier = request.getEmail();
        if (identifier == null || identifier.isBlank()) {
            identifier = request.getUsername();
        }

        if (identifier == null || identifier.isBlank() || request.getPassword() == null || request.getPassword().isBlank()) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Email/username and password are required");
            return ResponseEntity.badRequest().body(error);
        }

        identifier = identifier.trim();
        String rawPassword = request.getPassword().trim();

        // Check if user exists in database
        Optional<User> optionalUser = userRepository.findByEmail(identifier);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean matches = passwordEncoder.matches(rawPassword, user.getPassword())
                    || rawPassword.equals(user.getPassword());

            if (matches) {
                String token = jwtService.generateToken(user.getEmail());
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("email", user.getEmail());
                response.put("token", token);
                response.put("user", Map.of(
                        "id", user.getId(),
                        "name", user.getName() != null ? user.getName() : user.getEmail(),
                        "email", user.getEmail(),
                        "role", user.getRole() != null ? user.getRole() : "ADMIN"
                ));
                return ResponseEntity.ok(response);
            }
        }

        // Support default admin fallback
        if ("admin@poultry.com".equalsIgnoreCase(identifier) && "Admin@123".equals(rawPassword)) {
            User admin = userRepository.findByEmail("admin@poultry.com").orElseGet(() -> {
                User u = new User();
                u.setName("Farm Admin");
                u.setEmail("admin@poultry.com");
                u.setPassword(passwordEncoder.encode("Admin@123"));
                u.setRole("ADMIN");
                return userRepository.save(u);
            });

            String token = jwtService.generateToken(admin.getEmail());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("email", admin.getEmail());
            response.put("token", token);
            response.put("user", Map.of(
                    "id", admin.getId(),
                    "name", admin.getName(),
                    "email", admin.getEmail(),
                    "role", admin.getRole()
            ));
            return ResponseEntity.ok(response);
        }

        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", "Invalid email or password");
        return ResponseEntity.status(401).body(error);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String identifier = request.getEmail();
        if (identifier == null || identifier.isBlank()) {
            identifier = request.getUsername();
        }

        if (identifier == null || identifier.isBlank() || request.getPassword() == null || request.getPassword().isBlank()) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Email/username and password are required");
            return ResponseEntity.badRequest().body(error);
        }

        identifier = identifier.trim();
        String rawPassword = request.getPassword().trim();

        if (userRepository.findByEmail(identifier).isPresent()) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "An account with this email already exists");
            return ResponseEntity.badRequest().body(error);
        }

        String name = request.getName();
        if (name == null || name.isBlank()) {
            name = identifier.contains("@") ? identifier.substring(0, identifier.indexOf('@')) : identifier;
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(identifier);
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        newUser.setRole("ADMIN");

        User savedUser = userRepository.save(newUser);
        String token = jwtService.generateToken(savedUser.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Account registered successfully");
        response.put("email", savedUser.getEmail());
        response.put("token", token);
        response.put("user", Map.of(
                "id", savedUser.getId(),
                "name", savedUser.getName(),
                "email", savedUser.getEmail(),
                "role", savedUser.getRole()
        ));

        return ResponseEntity.ok(response);
    }

    public static class LoginRequest {
        private String email;
        private String username;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class RegisterRequest {
        private String name;
        private String email;
        private String username;
        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}