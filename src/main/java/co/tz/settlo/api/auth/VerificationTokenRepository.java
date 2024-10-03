package co.tz.settlo.api.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {
    Optional<VerificationToken> findByEmail(String email);
    Optional<VerificationToken> findByTokenId(UUID token);
    void deleteByEmail(String email);
}
