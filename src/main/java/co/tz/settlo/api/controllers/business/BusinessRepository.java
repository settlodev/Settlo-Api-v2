package co.tz.settlo.api.controllers.business;

import co.tz.settlo.api.controllers.country.Country;
import co.tz.settlo.api.controllers.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface BusinessRepository extends JpaRepository<Business, UUID>, JpaSpecificationExecutor<Business> {

    //List<Location> findAllByUserId(UUID userId);

    Optional<Business> findFirstById(UUID businessId);

    List<Business> findAllByUserId(UUID userId);

    Business findFirstByUser(User user);

    Business findFirstByCountry(Country country);

}
