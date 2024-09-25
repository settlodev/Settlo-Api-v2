package co.tz.settlo.api.brand;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BrandRepository extends JpaRepository<Brand, UUID>, JpaSpecificationExecutor<Brand> {

    List<Brand> findAllByLocationId(UUID locationId);

    boolean existsByNameIgnoreCase(String name);

}
