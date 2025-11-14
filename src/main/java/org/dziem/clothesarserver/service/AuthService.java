package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.AuthResponse;
import org.dziem.clothesarserver.dto.LoginRequest;
import org.dziem.clothesarserver.dto.RegisterRequest;

public interface AuthService {

    boolean register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    boolean logout(String userId);

    String me();

}
