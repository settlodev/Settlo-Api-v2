package co.tz.settlo.api.controllers.product;

import co.tz.settlo.api.controllers.brand.Brand;
import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.department.Department;
import co.tz.settlo.api.controllers.location.Location;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    List<Product> findAllByLocationId(UUID locationId);

    Product findFirstByBusiness(Business business);

    Product findFirstByLocation(Location location);

    Product findFirstByDepartment(Department department);

    Product findFirstByBrand(Brand brand);

    boolean existsByNameIgnoreCase(String name);

    boolean existsBySlugIgnoreCase(String slug);

}
