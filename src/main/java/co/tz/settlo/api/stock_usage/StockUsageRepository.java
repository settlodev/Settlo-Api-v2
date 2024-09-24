package co.tz.settlo.api.stock_usage;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.location.Location;
import co.tz.settlo.api.unit.Unit;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StockUsageRepository extends JpaRepository<StockUsage, UUID> {

    StockUsage findFirstByUnit(Unit unit);

    StockUsage findFirstByBusiness(Business business);

    StockUsage findFirstByLocation(Location location);

}
