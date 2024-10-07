package co.tz.settlo.api.controllers.supplier;

import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.location.Location;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface  SupplierRepository  extends JpaRepository<Supplier, UUID>, JpaSpecificationExecutor<Supplier> {

    List<Supplier> findAllByLocationId(UUID locationId);

    Supplier findFirstByBusiness(Business business);

    Supplier findFirstByLocation(Location location);

    boolean existsByNameIgnoreCase(String name);

}
