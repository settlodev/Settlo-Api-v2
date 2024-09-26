package co.tz.settlo.api.stock_intake;

import co.tz.settlo.api.staff.Staff;
import co.tz.settlo.api.stock_variant.StockVariant;
import co.tz.settlo.api.supplier.Supplier;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface StockIntakeRepository extends JpaRepository<StockIntake, UUID>, JpaSpecificationExecutor<StockIntake> {

    StockIntake findFirstByStockVariant(StockVariant stockVariant);

    StockIntake findFirstByStaff(Staff staff);

    StockIntake findFirstBySupplier(Supplier supplier);

}
