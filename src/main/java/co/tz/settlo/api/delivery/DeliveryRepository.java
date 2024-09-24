package co.tz.settlo.api.delivery;

import co.tz.settlo.api.order_item.OrderItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    Delivery findFirstByOrderItem(OrderItem orderItem);

}
