package co.tz.settlo.api.business;

import co.tz.settlo.api.country.Country;
import co.tz.settlo.api.user.User;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BusinessRepository extends JpaRepository<Business, UUID> {
    List<Business> findAllByUserId(UUID userId);

    Business findFirstByUser(User user);

    Business findFirstByCountry(Country country);

}
