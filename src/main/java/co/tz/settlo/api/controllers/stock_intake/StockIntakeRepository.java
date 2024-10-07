package co.tz.settlo.api.controllers.stock_intake;

import co.tz.settlo.api.controllers.staff.Staff;
import co.tz.settlo.api.controllers.stock_variant.StockVariant;
import co.tz.settlo.api.controllers.supplier.Supplier;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface StockIntakeRepository extends JpaRepository<StockIntake, UUID>, JpaSpecificationExecutor<StockIntake> {

    StockIntake findFirstByStockVariant(StockVariant stockVariant);

    StockIntake findFirstByStaff(Staff staff);

    StockIntake findFirstBySupplier(Supplier supplier);

}
