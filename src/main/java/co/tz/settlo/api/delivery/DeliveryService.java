package co.tz.settlo.api.delivery;

import co.tz.settlo.api.order_item.OrderItem;
import co.tz.settlo.api.order_item.OrderItemRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderItemRepository orderItemRepository;

    public DeliveryService(final DeliveryRepository deliveryRepository,
            final OrderItemRepository orderItemRepository) {
        this.deliveryRepository = deliveryRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<DeliveryDTO> findAll() {
        final List<Delivery> deliveries = deliveryRepository.findAll(Sort.by("id"));
        return deliveries.stream()
                .map(delivery -> mapToDTO(delivery, new DeliveryDTO()))
                .toList();
    }

    public DeliveryDTO get(final UUID id) {
        return deliveryRepository.findById(id)
                .map(delivery -> mapToDTO(delivery, new DeliveryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final DeliveryDTO deliveryDTO) {
        final Delivery delivery = new Delivery();
        mapToEntity(deliveryDTO, delivery);
        return deliveryRepository.save(delivery).getId();
    }

    public void update(final UUID id, final DeliveryDTO deliveryDTO) {
        final Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(deliveryDTO, delivery);
        deliveryRepository.save(delivery);
    }

    public void delete(final UUID id) {
        deliveryRepository.deleteById(id);
    }

    private DeliveryDTO mapToDTO(final Delivery delivery, final DeliveryDTO deliveryDTO) {
        deliveryDTO.setId(delivery.getId());
        deliveryDTO.setComment(delivery.getComment());
        deliveryDTO.setStatus(delivery.getStatus());
        deliveryDTO.setIsArchived(delivery.getIsArchived());
        deliveryDTO.setCanDelete(delivery.getCanDelete());
        deliveryDTO.setOrderStatus(delivery.getOrderStatus());
        deliveryDTO.setOrderItem(delivery.getOrderItem() == null ? null : delivery.getOrderItem().getId());
        return deliveryDTO;
    }

    private Delivery mapToEntity(final DeliveryDTO deliveryDTO, final Delivery delivery) {
        delivery.setComment(deliveryDTO.getComment());
        delivery.setStatus(deliveryDTO.getStatus());
        delivery.setIsArchived(deliveryDTO.getIsArchived());
        delivery.setCanDelete(deliveryDTO.getCanDelete());
        delivery.setOrderStatus(deliveryDTO.getOrderStatus());
        final OrderItem orderItem = deliveryDTO.getOrderItem() == null ? null : orderItemRepository.findById(deliveryDTO.getOrderItem())
                .orElseThrow(() -> new NotFoundException("orderItem not found"));
        delivery.setOrderItem(orderItem);
        return delivery;
    }

}
