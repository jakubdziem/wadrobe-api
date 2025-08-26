package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean userExists(String userId) {
        return userRepository.findByUserId(UUID.fromString(userId)).isPresent();
    }

    @Override
    public boolean userExists(UUID userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

}
