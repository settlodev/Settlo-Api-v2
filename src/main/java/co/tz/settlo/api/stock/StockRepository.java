package co.tz.settlo.api.stock;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StockRepository extends JpaRepository<Stock, Long> {

    Stock findFirstByBusiness(Business business);

    Stock findFirstByLocation(Location location);

}
