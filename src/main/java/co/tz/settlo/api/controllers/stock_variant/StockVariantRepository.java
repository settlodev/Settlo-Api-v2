package co.tz.settlo.api.controllers.stock_variant;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface StockVariantRepository extends JpaRepository<StockVariant, UUID>, JpaSpecificationExecutor<StockVariant> {

    List<StockVariant> findAllByLocationId(UUID locationId);

}
