package co.tz.settlo.api.item_return;

import co.tz.settlo.api.order_item.OrderItem;
import co.tz.settlo.api.staff.Staff;
import co.tz.settlo.api.stock_variant.StockVariant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemReturnRepository extends JpaRepository<ItemReturn, UUID> {

    ItemReturn findFirstByOrderItem(OrderItem orderItem);

    ItemReturn findFirstByStaff(Staff staff);

    ItemReturn findFirstByApprovedBy(Staff staff);

    ItemReturn findFirstByStockVariant(StockVariant stockVariant);

}
