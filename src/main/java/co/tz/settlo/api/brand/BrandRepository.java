package co.tz.settlo.api.brand;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BrandRepository extends JpaRepository<Brand, UUID> {

    boolean existsByNameIgnoreCase(String name);

}
