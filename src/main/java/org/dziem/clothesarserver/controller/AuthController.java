package org.dziem.clothesarserver.controller;

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

    @GetMapping("/me")
    public ResponseEntity<Void> me() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<Void> logout(@PathVariable String userId) {
        //smth
        return ResponseEntity.ok().build();
    }
//    logout endpoint success to wtedy usunie lokanie usuwanie tokenow, tylko success
}
