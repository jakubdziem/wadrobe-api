package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.AuthResponse;
import org.dziem.clothesarserver.dto.LoginRequest;
import org.dziem.clothesarserver.dto.RegisterRequest;
import org.dziem.clothesarserver.model.User;
import org.dziem.clothesarserver.repository.UserRepository;
import org.dziem.clothesarserver.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserService userServiceMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private JwtProvider jwtProviderMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @Mock
    private StringRedisTemplate redisTemplateMock;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(userServiceMock, userRepositoryMock, jwtProviderMock, passwordEncoderMock, redisTemplateMock);
    }

    @Test
    void register_shouldReturnFalse_whenEmailAlreadyExists() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password1!");

        when(userRepositoryMock.findByEmail("test@example.com"))
                .thenReturn(Optional.of(new User()));

        // when
        boolean result = authService.register(request);

        // then
        assertThat(result).isFalse();
        verify(userRepositoryMock, never()).save(any());
    }

    @Test
    void register_shouldSaveUser_whenEmailNotExists() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");
        request.setPassword("Password1!");

        when(userRepositoryMock.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(passwordEncoderMock.encode("Password1!")).thenReturn("encodedPassword");

        // when
        boolean result = authService.register(request);

        // then
        assertThat(result).isTrue();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryMock).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("new@example.com");
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
        // given
        LoginRequest request = new LoginRequest();
        request.setEmail("missing@example.com");
        request.setPassword("Password1!");

        when(userRepositoryMock.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void login_shouldThrowException_whenPasswordDoesNotMatch() {
        // given
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepositoryMock.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoderMock.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // when / then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    void login_shouldReturnAuthResponse_whenCredentialsValid() {
        // given
        LoginRequest request = new LoginRequest();
        request.setEmail("valid@example.com");
        request.setPassword("Password1!");

        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setUserId(userId);
        user.setEmail("valid@example.com");
        user.setPassword("encodedPassword");

        SetOperations<String, String> setOperations = Mockito.mock(SetOperations.class);
        when(redisTemplateMock.opsForSet()).thenReturn(setOperations);
        when(userRepositoryMock.findByEmail("valid@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoderMock.matches("Password1!", "encodedPassword")).thenReturn(true);
        when(jwtProviderMock.generateToken(userId.toString())).thenReturn("jwt-token");

        // when
        AuthResponse response = authService.login(request);

        // then
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getAccessToken()).isEqualTo("jwt-token");
        assertThat(response.getEmail()).isEqualTo("valid@example.com");

        verify(setOperations).add("userTokens:" + userId, "jwt-token");
    }

    @Test
    void logout_shouldReturnFalse_whenUserDoesNotExist() {
        // given
        String userId = UUID.randomUUID().toString();
        when(userServiceMock.userExists(userId)).thenReturn(false);

        // when / then
        assertThatThrownBy(() -> authService.logout(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
        verify(redisTemplateMock, never()).delete(anyString());
    }

    @Test
    void logout_shouldDeleteTokensAndReturnTrue_whenUserExists() {
        // given
        String userId = UUID.randomUUID().toString();
        when(userServiceMock.userExists(userId)).thenReturn(true);

        // when
        boolean result = authService.logout(userId);

        // then
        assertThat(result).isTrue();
        verify(redisTemplateMock).delete("userTokens:" + userId);
    }
}
