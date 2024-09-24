package co.tz.settlo.api.subscription;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    boolean existsByPackageCodeIgnoreCase(String packageCode);

}
