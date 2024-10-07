package co.tz.settlo.api.controllers.unit;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UnitRepository extends JpaRepository<Unit, UUID> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsBySymbolIgnoreCase(String symbol);

}
