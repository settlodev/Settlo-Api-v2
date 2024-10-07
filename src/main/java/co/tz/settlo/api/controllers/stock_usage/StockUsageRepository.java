package co.tz.settlo.api.controllers.stock_usage;

import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.location.Location;
import co.tz.settlo.api.controllers.unit.Unit;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface StockUsageRepository extends JpaRepository<StockUsage, UUID>, JpaSpecificationExecutor<StockUsage> {
    List<StockUsage> findAllByLocationId(UUID locationId);

    StockUsage findFirstByUnit(Unit unit);

    StockUsage findFirstByBusiness(Business business);

    StockUsage findFirstByLocation(Location location);

}
