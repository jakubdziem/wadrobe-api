package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.model.User;
import org.dziem.clothesarserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void userExistsByString_shouldReturnTrue_whenUserExists() {
        // Given
        UUID id = UUID.randomUUID();
        when(userRepository.findByUserId(id)).thenReturn(Optional.of(User.builder().userId(id).build()));

        // When
        boolean exists = userService.userExists(id.toString());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void userExistsByString_shouldReturnFalse_whenUserDoesNotExist() {
        // Given
        UUID id = UUID.randomUUID();
        when(userRepository.findByUserId(id)).thenReturn(Optional.empty());

        // When
        boolean exists = userService.userExists(id.toString());

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void userExistsByUUID_shouldReturnTrue_whenUserExists() {
        // Given
        UUID id = UUID.randomUUID();
        when(userRepository.findByUserId(id)).thenReturn(Optional.of(User.builder().userId(id).build()));

        // When
        boolean exists = userService.userExists(id);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void userExistsByUUID_shouldReturnFalse_whenUserDoesNotExist() {
        // Given
        UUID id = UUID.randomUUID();
        when(userRepository.findByUserId(id)).thenReturn(Optional.empty());

        // When
        boolean exists = userService.userExists(id);

        // Then
        assertThat(exists).isFalse();
    }
}
