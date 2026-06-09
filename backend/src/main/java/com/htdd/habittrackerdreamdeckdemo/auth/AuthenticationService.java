package com.htdd.habittrackerdreamdeckdemo.auth;

import com.htdd.habittrackerdreamdeckdemo.config.JwtService;
import com.htdd.habittrackerdreamdeckdemo.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        PasswordPolicy.validate(request.getPassword());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Цей Email вже зареєстровано");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Цей логін вже зайнятий");
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .registrationDate(java.time.LocalDateTime.now())
                .build();
        var savedUser = userRepository.save(user);
        savedUser.setProfilePicture(DefaultImagePaths.defaultAvatarFor(savedUser.getId()));
        savedUser = userRepository.save(savedUser);
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = createRefreshToken(savedUser);

        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken.getToken()).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );

        var user = userRepository.findByEmailOrUsername(request.getLogin(), request.getLogin())
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = createRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthenticationResponse refreshToken(String requestRefreshToken) {
        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(token -> {
                    if (token.getExpiryDate().isBefore(Instant.now())) {
                        refreshTokenRepository.delete(token);
                        throw new RuntimeException("Refresh token expired");
                    }
                    return token;
                })
                .map(token -> {
                    String accessToken = jwtService.generateToken(token.getUser());
                    return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(requestRefreshToken).build();
                })
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    private RefreshToken createRefreshToken(User user) {
        var refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plus(30, ChronoUnit.DAYS))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }
}