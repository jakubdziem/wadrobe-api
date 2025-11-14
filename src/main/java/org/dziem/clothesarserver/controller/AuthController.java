package org.dziem.clothesarserver.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.dziem.clothesarserver.dto.AuthResponse;
import org.dziem.clothesarserver.dto.LoginRequest;
import org.dziem.clothesarserver.dto.RegisterRequest;
import org.dziem.clothesarserver.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<String> me() {
        return ResponseEntity.ok(authService.me());
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout/{userId}")
    public ResponseEntity<Void> logout(@PathVariable String userId) {
        authService.logout(userId);
        return ResponseEntity.ok().build();
    }
}
