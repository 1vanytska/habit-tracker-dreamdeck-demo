package com.htdd.habittrackerdreamdeckdemo.auth;

import com.htdd.habittrackerdreamdeckdemo.config.JwtService;
import com.htdd.habittrackerdreamdeckdemo.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User mockUser;
    private RegisterRequest registerRequest;
    private AuthenticationRequest authRequest;
    private RefreshToken validRefreshToken;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(UUID.randomUUID())
                .username("testUser")
                .email("test@example.com")
                .password("hashedPassword")
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .build();

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("plainPassword!");

        authRequest = new AuthenticationRequest();
        authRequest.setLogin("test@example.com");
        authRequest.setPassword("plainPassword");

        validRefreshToken = RefreshToken.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .user(mockUser)
                .expiryDate(Instant.now().plus(30, ChronoUnit.DAYS))
                .build();
    }

    @Test
    void register_Positive_ShouldCreateUserAndReturnTokens() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("mocked-jwt-token");

        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(validRefreshToken);

        AuthenticationResponse response = authenticationService.register(registerRequest);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getAccessToken());
        assertEquals(validRefreshToken.getToken(), response.getRefreshToken());

        verify(userRepository, times(2)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("plainPassword!");
    }

    @Test
    void register_Negative_ShouldThrowException_WhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.register(registerRequest);
        });

        assertEquals("Цей Email вже зареєстровано", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticate_Positive_ShouldReturnTokens_WhenCredentialsAreValid() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmailOrUsername(authRequest.getLogin(), authRequest.getLogin())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(mockUser)).thenReturn("mocked-jwt-token");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(validRefreshToken);

        AuthenticationResponse response = authenticationService.authenticate(authRequest);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getAccessToken());

        verify(authenticationManager, times(1)).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );
    }

    @Test
    void refreshToken_Positive_ShouldReturnNewJwt_WhenRefreshTokenIsValid() {
        String tokenString = validRefreshToken.getToken();
        when(refreshTokenRepository.findByToken(tokenString)).thenReturn(Optional.of(validRefreshToken));
        when(jwtService.generateToken(mockUser)).thenReturn("new-mocked-jwt-token");

        AuthenticationResponse response = authenticationService.refreshToken(tokenString);

        assertNotNull(response);
        assertEquals("new-mocked-jwt-token", response.getAccessToken());
        assertEquals(tokenString, response.getRefreshToken());
    }

    @Test
    void refreshToken_Negative_ShouldThrowExceptionAndDeleteToken_WhenTokenIsExpired() {
        String tokenString = "expired-token";
        RefreshToken expiredToken = RefreshToken.builder()
                .token(tokenString)
                .user(mockUser)
                .expiryDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .build();

        when(refreshTokenRepository.findByToken(tokenString)).thenReturn(Optional.of(expiredToken));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.refreshToken(tokenString);
        });

        assertEquals("Refresh token expired", exception.getMessage());
        verify(refreshTokenRepository, times(1)).delete(expiredToken);
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void refreshToken_Negative_ShouldThrowException_WhenTokenNotFound() {
        String invalidToken = "invalid-token";
        when(refreshTokenRepository.findByToken(invalidToken)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.refreshToken(invalidToken);
        });

        assertEquals("Refresh token not found", exception.getMessage());
    }

    @Test
    void logout_Positive_ShouldDeleteRefreshToken() {
        String tokenString = "token-to-delete";

        authenticationService.logout(tokenString);

        verify(refreshTokenRepository, times(1)).deleteByToken(tokenString);
    }
}