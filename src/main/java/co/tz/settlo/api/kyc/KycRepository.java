package co.tz.settlo.api.kyc;

import co.tz.settlo.api.user.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface KycRepository extends JpaRepository<Kyc, UUID> {

    Kyc findFirstByUser(User user);

}
