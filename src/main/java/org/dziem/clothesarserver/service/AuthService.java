package org.dziem.clothesarserver.service;

import lombok.RequiredArgsConstructor;
import org.dziem.clothesarserver.dto.AuthResponse;
import org.dziem.clothesarserver.dto.LoginRequest;
import org.dziem.clothesarserver.dto.RegisterRequest;
import org.dziem.clothesarserver.model.User;
import org.dziem.clothesarserver.repository.UserRepository;
import org.dziem.clothesarserver.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public boolean register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) return false;
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return true;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtProvider.generateToken(user.getEmail());

        return new AuthResponse(user.getUserId(), accessToken, request.getEmail());
    }
}

