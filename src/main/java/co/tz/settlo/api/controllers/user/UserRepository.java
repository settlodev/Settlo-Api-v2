package co.tz.settlo.api.controllers.user;

import co.tz.settlo.api.controllers.country.Country;

import java.util.Optional;
import java.util.UUID;

import co.tz.settlo.api.controllers.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, UUID> {
    User findFirstByRole(Role role);

    Optional<User> findFirstByEmail(String email);

    User findFirstByCountry(Country country);

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByEmailIgnoreCase(String email);

}
