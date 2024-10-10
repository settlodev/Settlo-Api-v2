package co.tz.settlo.api.controllers.pending_product;

import co.tz.settlo.api.controllers.brand.Brand;
import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.department.Department;
import co.tz.settlo.api.controllers.location.Location;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PendingProductRepository extends JpaRepository<PendingProduct, UUID>, JpaSpecificationExecutor<PendingProduct> {

    List<PendingProduct> findAllByLocationId(UUID locationId);

    Optional<PendingProduct> findByName(String productName);

    PendingProduct findFirstByBusiness(Business business);

    PendingProduct findFirstByLocation(Location location);

    PendingProduct findFirstByDepartment(Department department);

    PendingProduct findFirstByBrand(Brand brand);

    boolean existsByNameIgnoreCase(String name);

    boolean existsBySlugIgnoreCase(String slug);

}
