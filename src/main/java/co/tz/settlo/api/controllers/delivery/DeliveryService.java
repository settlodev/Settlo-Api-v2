package co.tz.settlo.api.controllers.delivery;

import co.tz.settlo.api.controllers.order_item.OrderItem;
import co.tz.settlo.api.controllers.order_item.OrderItemRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderItemRepository orderItemRepository;

    public DeliveryService(final DeliveryRepository deliveryRepository,
            final OrderItemRepository orderItemRepository) {
        this.deliveryRepository = deliveryRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional(readOnly = true)
    public List<DeliveryDTO> findAll(final UUID locationId) {
        final List<Delivery> deliveries = deliveryRepository.findAllByLocationId(locationId);
        return deliveries.stream()
                .map(delivery -> mapToDTO(delivery, new DeliveryDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public DeliveryDTO get(final UUID id) {
        return deliveryRepository.findById(id)
                .map(delivery -> mapToDTO(delivery, new DeliveryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<DeliveryDTO> searchAll(SearchRequest request) {
        SearchSpecification<Delivery> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Delivery> deliveryPage = deliveryRepository.findAll(specification, pageable);

        return deliveryPage.map(customer -> mapToDTO(customer, new DeliveryDTO()));
    }

    @Transactional
    public UUID create(final DeliveryDTO deliveryDTO) {
        final Delivery delivery = new Delivery();
        mapToEntity(deliveryDTO, delivery);
        return deliveryRepository.save(delivery).getId();
    }

    @Transactional
    public void update(final UUID id, final DeliveryDTO deliveryDTO) {
        final Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(deliveryDTO, delivery);
        deliveryRepository.save(delivery);
    }

    @Transactional
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
