package co.tz.settlo.api.controllers.item_return;

import co.tz.settlo.api.controllers.order_item.OrderItem;
import co.tz.settlo.api.controllers.staff.Staff;
import co.tz.settlo.api.controllers.stock_variant.StockVariant;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ItemReturnRepository extends JpaRepository<ItemReturn, UUID>, JpaSpecificationExecutor<ItemReturn> {
    List<ItemReturn> findAllByLocationId(UUID locationId);

    ItemReturn findFirstByOrderItem(OrderItem orderItem);

    ItemReturn findFirstByStaff(Staff staff);

    ItemReturn findFirstByApprovedBy(Staff staff);

    ItemReturn findFirstByStockVariant(StockVariant stockVariant);

}
