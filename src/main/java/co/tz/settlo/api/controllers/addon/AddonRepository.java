package co.tz.settlo.api.controllers.addon;

import co.tz.settlo.api.controllers.order_item.OrderItem;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface AddonRepository extends JpaRepository<Addon, UUID>, JpaSpecificationExecutor<Addon> {
    List<Addon> findAllByLocationId(UUID locationId);

    Addon findFirstByOrderItem(OrderItem orderItem);

    boolean existsByTitleIgnoreCase(String title);

}
