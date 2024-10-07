package co.tz.settlo.api.controllers.delivery;

import co.tz.settlo.api.controllers.order_item.OrderItem;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID>, JpaSpecificationExecutor<Delivery> {
    List<Delivery> findAllByLocationId(UUID locationId);

    Delivery findFirstByOrderItem(OrderItem orderItem);

}
