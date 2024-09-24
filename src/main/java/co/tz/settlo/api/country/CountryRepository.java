package co.tz.settlo.api.country;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CountryRepository extends JpaRepository<Country, UUID> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByCodeIgnoreCase(String code);

}
