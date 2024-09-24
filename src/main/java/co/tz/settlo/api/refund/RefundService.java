package co.tz.settlo.api.refund;

import co.tz.settlo.api.order_item.OrderItem;
import co.tz.settlo.api.order_item.OrderItemRepository;
import co.tz.settlo.api.staff.Staff;
import co.tz.settlo.api.staff.StaffRepository;
import co.tz.settlo.api.stock_variant.StockVariant;
import co.tz.settlo.api.stock_variant.StockVariantRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RefundService {

    private final RefundRepository refundRepository;
    private final OrderItemRepository orderItemRepository;
    private final StaffRepository staffRepository;
    private final StockVariantRepository stockVariantRepository;

    public RefundService(final RefundRepository refundRepository,
            final OrderItemRepository orderItemRepository, final StaffRepository staffRepository,
            final StockVariantRepository stockVariantRepository) {
        this.refundRepository = refundRepository;
        this.orderItemRepository = orderItemRepository;
        this.staffRepository = staffRepository;
        this.stockVariantRepository = stockVariantRepository;
    }

    public List<RefundDTO> findAll() {
        final List<Refund> refunds = refundRepository.findAll(Sort.by("id"));
        return refunds.stream()
                .map(refund -> mapToDTO(refund, new RefundDTO()))
                .toList();
    }

    public RefundDTO get(final UUID id) {
        return refundRepository.findById(id)
                .map(refund -> mapToDTO(refund, new RefundDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final RefundDTO refundDTO) {
        final Refund refund = new Refund();
        mapToEntity(refundDTO, refund);
        return refundRepository.save(refund).getId();
    }

    public void update(final UUID id, final RefundDTO refundDTO) {
        final Refund refund = refundRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(refundDTO, refund);
        refundRepository.save(refund);
    }

    public void delete(final UUID id) {
        refundRepository.deleteById(id);
    }

    private RefundDTO mapToDTO(final Refund refund, final RefundDTO refundDTO) {
        refundDTO.setId(refund.getId());
        refundDTO.setDateOfRefund(refund.getDateOfRefund());
        refundDTO.setReason(refund.getReason());
        refundDTO.setStatus(refund.getStatus());
        refundDTO.setIsArchived(refund.getIsArchived());
        refundDTO.setCanDelete(refund.getCanDelete());
        refundDTO.setOrderItem(refund.getOrderItem() == null ? null : refund.getOrderItem().getId());
        refundDTO.setStaff(refund.getStaff() == null ? null : refund.getStaff().getId());
        refundDTO.setApprovedBy(refund.getApprovedBy() == null ? null : refund.getApprovedBy().getId());
        refundDTO.setStockVariant(refund.getStockVariant() == null ? null : refund.getStockVariant().getId());
        return refundDTO;
    }

    private Refund mapToEntity(final RefundDTO refundDTO, final Refund refund) {
        refund.setDateOfRefund(refundDTO.getDateOfRefund());
        refund.setReason(refundDTO.getReason());
        refund.setStatus(refundDTO.getStatus());
        refund.setIsArchived(refundDTO.getIsArchived());
        refund.setCanDelete(refundDTO.getCanDelete());
        final OrderItem orderItem = refundDTO.getOrderItem() == null ? null : orderItemRepository.findById(refundDTO.getOrderItem())
                .orElseThrow(() -> new NotFoundException("orderItem not found"));
        refund.setOrderItem(orderItem);
        final Staff staff = refundDTO.getStaff() == null ? null : staffRepository.findById(refundDTO.getStaff())
                .orElseThrow(() -> new NotFoundException("staff not found"));
        refund.setStaff(staff);
        final Staff approvedBy = refundDTO.getApprovedBy() == null ? null : staffRepository.findById(refundDTO.getApprovedBy())
                .orElseThrow(() -> new NotFoundException("approvedBy not found"));
        refund.setApprovedBy(approvedBy);
        final StockVariant stockVariant = refundDTO.getStockVariant() == null ? null : stockVariantRepository.findById(refundDTO.getStockVariant())
                .orElseThrow(() -> new NotFoundException("stockVariant not found"));
        refund.setStockVariant(stockVariant);
        return refund;
    }

}
