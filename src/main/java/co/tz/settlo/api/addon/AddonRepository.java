package co.tz.settlo.api.addon;

import co.tz.settlo.api.order_item.OrderItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddonRepository extends JpaRepository<Addon, UUID> {

    Addon findFirstByOrderItem(OrderItem orderItem);

    boolean existsByTitleIgnoreCase(String title);

}
