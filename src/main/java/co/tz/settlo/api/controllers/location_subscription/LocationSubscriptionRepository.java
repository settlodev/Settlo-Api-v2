package co.tz.settlo.api.controllers.location_subscription;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationSubscriptionRepository extends JpaRepository<LocationSubscription, UUID> {

//    boolean existsByPackageCodeIgnoreCase(String packageCode);

}
