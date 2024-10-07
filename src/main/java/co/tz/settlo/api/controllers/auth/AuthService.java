package co.tz.settlo.api.controllers.auth;

import co.tz.settlo.api.JwtHelper;
import co.tz.settlo.api.controllers.business.BusinessRepository;
import co.tz.settlo.api.controllers.user.User;
import co.tz.settlo.api.controllers.user.UserRepository;
import co.tz.settlo.api.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final BusinessRepository businessRepository;

    @Override
    @Cacheable(value = "userDetails", key = "#username")
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findFirstByEmail(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                //.roles(user.getRole().getName())
                .build();
    }

    @Transactional
    public void addLoginAttempt(String email, boolean success) {
        LoginAttempt loginAttempt = LoginAttempt.builder()
                .email(email)
                .success(success)
                .build();
        loginAttemptRepository.save(loginAttempt);
    }

    @Transactional
    public LoginResponseDTO login(String email, String password) {
        UserDetails userDetails = loadUserByUsername(email);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String accessToken = jwtHelper.generateAccessToken(email);
        String refreshToken = jwtHelper.generateRefreshToken(email);

        saveRefreshToken(email, refreshToken);

        return buildLoginResponse(userDetails, accessToken, refreshToken);
    }

    @Transactional
    public LoginResponseDTO refreshToken(String refreshToken) {
        String username = jwtHelper.extractUsername(refreshToken);
        UserDetails userDetails = loadUserByUsername(username);

        if (jwtHelper.validateToken(refreshToken, userDetails)) {
            String newAccessToken = jwtHelper.generateAccessToken(username);
            return buildLoginResponse(userDetails, newAccessToken, refreshToken);
        }

        throw new BadCredentialsException("Invalid refresh token");
    }

    @Transactional
    public UUID generatePasswordResetToken(String email) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByEmail(email)
                .orElse(new PasswordResetToken());

        passwordResetToken.setEmail(email);
        passwordResetToken.setUsed(false);
        passwordResetToken.setTokenId(UUID.randomUUID());
        passwordResetToken.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        PasswordResetToken savedToken = passwordResetTokenRepository.save(passwordResetToken);

        return savedToken.getTokenId();
    }

    @Transactional
    public void deleteByEmail(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<LoginAttempt> findRecentLoginAttempts(String email) {
        return loginAttemptRepository.findAllByEmail(email);
    }

    private LoginResponseDTO buildLoginResponse(UserDetails userDetails, String accessToken, String refreshToken) {
        User user = userRepository.findFirstByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        return LoginResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .picture(user.getAvatar())
                .authToken(accessToken)
                .refreshToken(refreshToken)
                .emailVerified(user.getEmailVerified())
                .phoneNumberVerified(user.getPhoneNumberVerified())
                .theme("light")
                .businessComplete(user.getIsBusinessRegistrationComplete())
                .locationComplete(user.getIsLocationRegistrationComplete())
                .build();
    }

    private void saveRefreshToken(String email, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .email(email)
                .token(token)
                .expiryDate(Instant.now().plusMillis(jwtHelper.getRefreshTokenValidity()))
                .build();

        refreshTokenRepository.save(refreshToken);
    }
}