package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean userExists(String userId) {
        return userRepository.findByUserId(UUID.fromString(userId)).isPresent();
    }

    public boolean userExists(UUID userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

}
