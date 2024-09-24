package co.tz.settlo.api.item_return;

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
public class ItemReturnService {

    private final ItemReturnRepository itemReturnRepository;
    private final OrderItemRepository orderItemRepository;
    private final StaffRepository staffRepository;
    private final StockVariantRepository stockVariantRepository;

    public ItemReturnService(final ItemReturnRepository itemReturnRepository,
            final OrderItemRepository orderItemRepository, final StaffRepository staffRepository,
            final StockVariantRepository stockVariantRepository) {
        this.itemReturnRepository = itemReturnRepository;
        this.orderItemRepository = orderItemRepository;
        this.staffRepository = staffRepository;
        this.stockVariantRepository = stockVariantRepository;
    }

    public List<ItemReturnDTO> findAll() {
        final List<ItemReturn> itemReturns = itemReturnRepository.findAll(Sort.by("id"));
        return itemReturns.stream()
                .map(itemReturn -> mapToDTO(itemReturn, new ItemReturnDTO()))
                .toList();
    }

    public ItemReturnDTO get(final UUID id) {
        return itemReturnRepository.findById(id)
                .map(itemReturn -> mapToDTO(itemReturn, new ItemReturnDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ItemReturnDTO itemReturnDTO) {
        final ItemReturn itemReturn = new ItemReturn();
        mapToEntity(itemReturnDTO, itemReturn);
        return itemReturnRepository.save(itemReturn).getId();
    }

    public void update(final UUID id, final ItemReturnDTO itemReturnDTO) {
        final ItemReturn itemReturn = itemReturnRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(itemReturnDTO, itemReturn);
        itemReturnRepository.save(itemReturn);
    }

    public void delete(final UUID id) {
        itemReturnRepository.deleteById(id);
    }

    private ItemReturnDTO mapToDTO(final ItemReturn itemReturn, final ItemReturnDTO itemReturnDTO) {
        itemReturnDTO.setId(itemReturn.getId());
        itemReturnDTO.setReason(itemReturn.getReason());
        itemReturnDTO.setDateOfReturn(itemReturn.getDateOfReturn());
        itemReturnDTO.setStatus(itemReturn.getStatus());
        itemReturnDTO.setCanDelete(itemReturn.getCanDelete());
        itemReturnDTO.setIsArchived(itemReturn.getIsArchived());
        itemReturnDTO.setOrderItem(itemReturn.getOrderItem() == null ? null : itemReturn.getOrderItem().getId());
        itemReturnDTO.setStaff(itemReturn.getStaff() == null ? null : itemReturn.getStaff().getId());
        itemReturnDTO.setApprovedBy(itemReturn.getApprovedBy() == null ? null : itemReturn.getApprovedBy().getId());
        itemReturnDTO.setStockVariant(itemReturn.getStockVariant() == null ? null : itemReturn.getStockVariant().getId());
        return itemReturnDTO;
    }

    private ItemReturn mapToEntity(final ItemReturnDTO itemReturnDTO, final ItemReturn itemReturn) {
        itemReturn.setReason(itemReturnDTO.getReason());
        itemReturn.setDateOfReturn(itemReturnDTO.getDateOfReturn());
        itemReturn.setStatus(itemReturnDTO.getStatus());
        itemReturn.setCanDelete(itemReturnDTO.getCanDelete());
        itemReturn.setIsArchived(itemReturnDTO.getIsArchived());
        final OrderItem orderItem = itemReturnDTO.getOrderItem() == null ? null : orderItemRepository.findById(itemReturnDTO.getOrderItem())
                .orElseThrow(() -> new NotFoundException("orderItem not found"));
        itemReturn.setOrderItem(orderItem);
        final Staff staff = itemReturnDTO.getStaff() == null ? null : staffRepository.findById(itemReturnDTO.getStaff())
                .orElseThrow(() -> new NotFoundException("staff not found"));
        itemReturn.setStaff(staff);
        final Staff approvedBy = itemReturnDTO.getApprovedBy() == null ? null : staffRepository.findById(itemReturnDTO.getApprovedBy())
                .orElseThrow(() -> new NotFoundException("approvedBy not found"));
        itemReturn.setApprovedBy(approvedBy);
        final StockVariant stockVariant = itemReturnDTO.getStockVariant() == null ? null : stockVariantRepository.findById(itemReturnDTO.getStockVariant())
                .orElseThrow(() -> new NotFoundException("stockVariant not found"));
        itemReturn.setStockVariant(stockVariant);
        return itemReturn;
    }

}
