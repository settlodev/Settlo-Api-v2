package co.tz.settlo.api.order_item;

import co.tz.settlo.api.staff.Staff;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    OrderItem findFirstByStaffId(Staff staff);

}
