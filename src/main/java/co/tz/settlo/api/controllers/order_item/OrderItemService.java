package co.tz.settlo.api.controllers.order_item;

import co.tz.settlo.api.controllers.addon.Addon;
import co.tz.settlo.api.controllers.addon.AddonRepository;
import co.tz.settlo.api.controllers.delivery.Delivery;
import co.tz.settlo.api.controllers.delivery.DeliveryRepository;
import co.tz.settlo.api.controllers.item_return.ItemReturn;
import co.tz.settlo.api.controllers.item_return.ItemReturnRepository;
import co.tz.settlo.api.controllers.refund.Refund;
import co.tz.settlo.api.controllers.refund.RefundRepository;
import co.tz.settlo.api.controllers.staff.Staff;
import co.tz.settlo.api.controllers.staff.StaffRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final StaffRepository staffRepository;
    private final DeliveryRepository deliveryRepository;
    private final AddonRepository addonRepository;
    private final RefundRepository refundRepository;
    private final ItemReturnRepository itemReturnRepository;

    public OrderItemService(final OrderItemRepository orderItemRepository,
            final StaffRepository staffRepository, final DeliveryRepository deliveryRepository,
            final AddonRepository addonRepository, final RefundRepository refundRepository,
            final ItemReturnRepository itemReturnRepository) {
        this.orderItemRepository = orderItemRepository;
        this.staffRepository = staffRepository;
        this.deliveryRepository = deliveryRepository;
        this.addonRepository = addonRepository;
        this.refundRepository = refundRepository;
        this.itemReturnRepository = itemReturnRepository;
    }

    public List<OrderItemDTO> findAll() {
        final List<OrderItem> orderItems = orderItemRepository.findAll(Sort.by("id"));
        return orderItems.stream()
                .map(orderItem -> mapToDTO(orderItem, new OrderItemDTO()))
                .toList();
    }

    public OrderItemDTO get(final UUID id) {
        return orderItemRepository.findById(id)
                .map(orderItem -> mapToDTO(orderItem, new OrderItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final OrderItemDTO orderItemDTO) {
        final OrderItem orderItem = new OrderItem();
        mapToEntity(orderItemDTO, orderItem);
        return orderItemRepository.save(orderItem).getId();
    }

    public void update(final UUID id, final OrderItemDTO orderItemDTO) {
        final OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderItemDTO, orderItem);
        orderItemRepository.save(orderItem);
    }

    public void delete(final UUID id) {
        orderItemRepository.deleteById(id);
    }

    private OrderItemDTO mapToDTO(final OrderItem orderItem, final OrderItemDTO orderItemDTO) {
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setDiscountAmount(orderItem.getDiscountAmount());
        orderItemDTO.setComment(orderItem.getComment());
        orderItemDTO.setPreparationStatus(orderItem.getPreparationStatus());
        orderItemDTO.setCanDelete(orderItem.getCanDelete());
        orderItemDTO.setStatus(orderItem.getStatus());
        orderItemDTO.setIsArchived(orderItem.getIsArchived());
        orderItemDTO.setStaffId(orderItem.getStaffId() == null ? null : orderItem.getStaffId().getId());
        return orderItemDTO;
    }

    private OrderItem mapToEntity(final OrderItemDTO orderItemDTO, final OrderItem orderItem) {
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setDiscountAmount(orderItemDTO.getDiscountAmount());
        orderItem.setComment(orderItemDTO.getComment());
        orderItem.setPreparationStatus(orderItemDTO.getPreparationStatus());
        orderItem.setCanDelete(orderItemDTO.getCanDelete());
        orderItem.setStatus(orderItemDTO.getStatus());
        orderItem.setIsArchived(orderItemDTO.getIsArchived());
        final Staff staffId = orderItemDTO.getStaffId() == null ? null : staffRepository.findById(orderItemDTO.getStaffId())
                .orElseThrow(() -> new NotFoundException("staffId not found"));
        orderItem.setStaffId(staffId);
        return orderItem;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Delivery orderItemDelivery = deliveryRepository.findFirstByOrderItem(orderItem);
        if (orderItemDelivery != null) {
            referencedWarning.setKey("orderItem.delivery.orderItem.referenced");
            referencedWarning.addParam(orderItemDelivery.getId());
            return referencedWarning;
        }
        final Addon orderItemAddon = addonRepository.findFirstByOrderItem(orderItem);
        if (orderItemAddon != null) {
            referencedWarning.setKey("orderItem.addon.orderItem.referenced");
            referencedWarning.addParam(orderItemAddon.getId());
            return referencedWarning;
        }
        final Refund orderItemRefund = refundRepository.findFirstByOrderItem(orderItem);
        if (orderItemRefund != null) {
            referencedWarning.setKey("orderItem.refund.orderItem.referenced");
            referencedWarning.addParam(orderItemRefund.getId());
            return referencedWarning;
        }
        final ItemReturn orderItemItemReturn = itemReturnRepository.findFirstByOrderItem(orderItem);
        if (orderItemItemReturn != null) {
            referencedWarning.setKey("orderItem.itemReturn.orderItem.referenced");
            referencedWarning.addParam(orderItemItemReturn.getId());
            return referencedWarning;
        }
        return null;
    }

}
