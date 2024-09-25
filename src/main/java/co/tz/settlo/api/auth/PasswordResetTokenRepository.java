package co.tz.settlo.api.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
    Optional<PasswordResetToken> findByEmail(String email);
    Optional<PasswordResetToken> deleteByEmail(String email);
}
