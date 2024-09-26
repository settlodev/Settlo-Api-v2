package co.tz.settlo.api.order_item;

import co.tz.settlo.api.staff.Staff;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface OrderItemRepository extends JpaRepository<OrderItem, UUID>, JpaSpecificationExecutor<OrderItem> {

    //List<OrderItem> findAllByLocationId(UUID locationId);

    OrderItem findFirstByStaffId(Staff staff);

}
