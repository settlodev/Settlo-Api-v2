package co.tz.settlo.api.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, UUID> {
    List<LoginAttempt> findAllByEmail(String email);
}
