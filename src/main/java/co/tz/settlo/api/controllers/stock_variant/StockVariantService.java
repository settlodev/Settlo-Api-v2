package co.tz.settlo.api.controllers.stock_variant;

import co.tz.settlo.api.controllers.item_return.ItemReturn;
import co.tz.settlo.api.controllers.item_return.ItemReturnRepository;
import co.tz.settlo.api.controllers.refund.Refund;
import co.tz.settlo.api.controllers.refund.RefundRepository;
import co.tz.settlo.api.controllers.stock.Stock;
import co.tz.settlo.api.controllers.stock.StockRepository;
import co.tz.settlo.api.controllers.stock_intake.StockIntake;
import co.tz.settlo.api.controllers.stock_intake.StockIntakeRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class StockVariantService {

    private final StockVariantRepository stockVariantRepository;
    private final StockRepository stockRepository;
    private final StockIntakeRepository stockIntakeRepository;
    private final RefundRepository refundRepository;
    private final ItemReturnRepository itemReturnRepository;

    public StockVariantService(final StockVariantRepository stockVariantRepository,
            final StockRepository stockRepository,
            final StockIntakeRepository stockIntakeRepository,
            final RefundRepository refundRepository,
            final ItemReturnRepository itemReturnRepository) {
        this.stockVariantRepository = stockVariantRepository;
        this.stockRepository = stockRepository;
        this.stockIntakeRepository = stockIntakeRepository;
        this.refundRepository = refundRepository;
        this.itemReturnRepository = itemReturnRepository;
    }

    @Transactional(readOnly = true)
    public List<StockVariantDTO> findAll() {
        final List<StockVariant> stockVariants = stockVariantRepository.findAll(Sort.by("id"));
        return stockVariants.stream()
                .map(stockVariant -> mapToDTO(stockVariant, new StockVariantDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public StockVariantDTO get(final UUID id) {
        return stockVariantRepository.findById(id)
                .map(stockVariant -> mapToDTO(stockVariant, new StockVariantDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<StockVariantDTO> searchAll(SearchRequest request) {
        SearchSpecification<StockVariant> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<StockVariant> stockVariantsPage = stockVariantRepository.findAll(specification, pageable);

        return stockVariantsPage.map(stockVariant -> mapToDTO(stockVariant, new StockVariantDTO()));
    }

    @Transactional
    public UUID create(final StockVariantDTO stockVariantDTO) {
        final StockVariant stockVariant = new StockVariant();
        mapToEntity(stockVariantDTO, stockVariant);
        return stockVariantRepository.save(stockVariant).getId();
    }

    @Transactional
    public void update(final UUID id, final StockVariantDTO stockVariantDTO) {
        final StockVariant stockVariant = stockVariantRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(stockVariantDTO, stockVariant);
        stockVariantRepository.save(stockVariant);
    }

    @Transactional
    public void delete(final UUID id) {
        stockVariantRepository.deleteById(id);
    }

    private StockVariantDTO mapToDTO(final StockVariant stockVariant,
            final StockVariantDTO stockVariantDTO) {
        stockVariantDTO.setId(stockVariant.getId());
        stockVariantDTO.setStartingValue(stockVariant.getStartingValue());
        stockVariantDTO.setActualValue(stockVariant.getActualValue());
        stockVariantDTO.setValue(stockVariant.getValue());
        stockVariantDTO.setStartingQuantity(stockVariant.getStartingQuantity());
        stockVariantDTO.setActualQuantity(stockVariant.getActualQuantity());
        stockVariantDTO.setQuantity(stockVariant.getQuantity());
        stockVariantDTO.setVariantName(stockVariant.getVariantName());
        stockVariantDTO.setAlertLevel(stockVariant.getAlertLevel());
        stockVariantDTO.setImageOption(stockVariant.getImageOption());
        stockVariantDTO.setStatus(stockVariant.getStatus());
        stockVariantDTO.setIsArchived(stockVariant.getIsArchived());
        stockVariantDTO.setCanDelete(stockVariant.getCanDelete());
        stockVariantDTO.setStock(stockVariant.getStock() == null ? null : stockVariant.getStock().getId());
        return stockVariantDTO;
    }

    private StockVariant mapToEntity(final StockVariantDTO stockVariantDTO,
            final StockVariant stockVariant) {
        stockVariant.setStartingValue(stockVariantDTO.getStartingValue());
        stockVariant.setActualValue(stockVariantDTO.getActualValue());
        stockVariant.setValue(stockVariantDTO.getValue());
        stockVariant.setStartingQuantity(stockVariantDTO.getStartingQuantity());
        stockVariant.setActualQuantity(stockVariantDTO.getActualQuantity());
        stockVariant.setQuantity(stockVariantDTO.getQuantity());
        stockVariant.setVariantName(stockVariantDTO.getVariantName());
        stockVariant.setAlertLevel(stockVariantDTO.getAlertLevel());
        stockVariant.setImageOption(stockVariantDTO.getImageOption());
        stockVariant.setStatus(stockVariantDTO.getStatus());
        stockVariant.setIsArchived(stockVariantDTO.getIsArchived());
        stockVariant.setCanDelete(stockVariantDTO.getCanDelete());
        final Stock stock = stockVariantDTO.getStock() == null ? null : stockRepository.findById(stockVariantDTO.getStock())
                .orElseThrow(() -> new NotFoundException("stock not found"));
        stockVariant.setStock(stock);
        return stockVariant;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final StockVariant stockVariant = stockVariantRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final StockIntake stockVariantStockIntake = stockIntakeRepository.findFirstByStockVariant(stockVariant);
        if (stockVariantStockIntake != null) {
            referencedWarning.setKey("stockVariant.stockIntake.stockVariant.referenced");
            referencedWarning.addParam(stockVariantStockIntake.getId());
            return referencedWarning;
        }
        final Refund stockVariantRefund = refundRepository.findFirstByStockVariant(stockVariant);
        if (stockVariantRefund != null) {
            referencedWarning.setKey("stockVariant.refund.stockVariant.referenced");
            referencedWarning.addParam(stockVariantRefund.getId());
            return referencedWarning;
        }
        final ItemReturn stockVariantItemReturn = itemReturnRepository.findFirstByStockVariant(stockVariant);
        if (stockVariantItemReturn != null) {
            referencedWarning.setKey("stockVariant.itemReturn.stockVariant.referenced");
            referencedWarning.addParam(stockVariantItemReturn.getId());
            return referencedWarning;
        }
        return null;
    }

}
