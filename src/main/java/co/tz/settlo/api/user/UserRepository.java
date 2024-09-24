package co.tz.settlo.api.user;

import co.tz.settlo.api.country.Country;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, UUID> {

    User findFirstByCountry(Country country);

    boolean existsByPrefix(Integer prefix);

    boolean existsByEmailIgnoreCase(String email);

}
