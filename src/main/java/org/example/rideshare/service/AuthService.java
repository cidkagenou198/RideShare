package org.example.rideshare.service;

import org.example.rideshare.dto.AuthResponse;
import org.example.rideshare.dto.LoginRequest;
import org.example.rideshare.dto.RegisterRequest;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.UserRepository;
import org.example.rideshare.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest request) {
        System.out.println("Attempting to register user: " + request.getUsername());
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new org.example.rideshare.exception.BadRequestException("Username already exists");
        }
        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole());
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        System.out.println("Login attempt for: " + request.getUsername());
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new org.example.rideshare.exception.NotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new org.example.rideshare.exception.BadRequestException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(token);
    }
}
