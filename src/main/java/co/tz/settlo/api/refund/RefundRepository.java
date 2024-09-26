package co.tz.settlo.api.refund;

import co.tz.settlo.api.order_item.OrderItem;
import co.tz.settlo.api.staff.Staff;
import co.tz.settlo.api.stock_variant.StockVariant;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface RefundRepository extends JpaRepository<Refund, UUID>, JpaSpecificationExecutor<Refund> {

    List<Refund> findAllByLocationId(UUID locationId);

    Refund findFirstByOrderItem(OrderItem orderItem);

    Refund findFirstByStaff(Staff staff);

    Refund findFirstByApprovedBy(Staff staff);

    Refund findFirstByStockVariant(StockVariant stockVariant);

}
