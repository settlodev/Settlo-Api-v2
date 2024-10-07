package co.tz.settlo.api.controllers.kyc;

import co.tz.settlo.api.controllers.user.User;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface KycRepository extends JpaRepository<Kyc, UUID> {
    Optional<Kyc> findByUser(User user);

    Kyc findFirstByUser(User user);
}
