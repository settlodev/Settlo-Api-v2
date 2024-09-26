package co.tz.settlo.api.stock;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;


public interface StockRepository extends JpaRepository<Stock, UUID>, JpaSpecificationExecutor<Stock> {

    List<Stock> findAllByLocationId(UUID locationId);

    Stock findFirstByBusiness(Business business);

    Stock findFirstByLocation(Location location);

}
