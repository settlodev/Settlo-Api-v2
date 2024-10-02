package co.tz.settlo.api.auth;

import co.tz.settlo.api.JwtHelper;
import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.business.BusinessRepository;
import co.tz.settlo.api.user.User;
import co.tz.settlo.api.user.UserRepository;
import co.tz.settlo.api.util.NotFoundException;
import lombok.RequiredArgsConstructor;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
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

    @Transactional(readOnly = true)
    public UUID generatePasswordResetToken(String email) {
        final Optional<PasswordResetToken> currentToken = passwordResetTokenRepository.findByEmail(email);
        currentToken.ifPresent(verificationToken -> passwordResetTokenRepository.deleteById(verificationToken.getId()));

        // Create and save new token
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUsed(false);
        passwordResetToken.setEmail(email);
        passwordResetToken.setExpiresAt(LocalDateTime.now().plusHours(1));

        PasswordResetToken savedToken = passwordResetTokenRepository.save(passwordResetToken);

        return savedToken.getId();
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

    @Transactional
    public void deleteByEmail(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }
}