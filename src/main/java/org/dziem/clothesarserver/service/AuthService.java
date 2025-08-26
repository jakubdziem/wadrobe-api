package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.AuthResponse;
import org.dziem.clothesarserver.dto.LoginRequest;
import org.dziem.clothesarserver.dto.RegisterRequest;
import org.dziem.clothesarserver.model.User;
import org.dziem.clothesarserver.repository.UserRepository;
import org.dziem.clothesarserver.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

@Service
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redisTemplate;

    public AuthService(UserService userService, UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, StringRedisTemplate redisTemplate) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
    }

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

        String userId = user.getUserId().toString();
        String accessToken = jwtProvider.generateToken(userId);
        String redisKey = "userTokens:" + userId;
        redisTemplate.opsForSet().add(redisKey, accessToken);
        return new AuthResponse(user.getUserId(), accessToken, request.getEmail());
    }

    public boolean logout(String userId) {
        if(userService.userExists(userId)) {
            String redisKey = "userTokens:" + userId;
            redisTemplate.delete(redisKey);
            return true;
        }
        return false;
    }
}

