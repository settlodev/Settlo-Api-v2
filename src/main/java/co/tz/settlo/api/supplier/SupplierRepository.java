package co.tz.settlo.api.supplier;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.location.Location;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    Supplier findFirstByBusiness(Business business);

    Supplier findFirstByLocation(Location location);

    boolean existsByNameIgnoreCase(String name);

}
