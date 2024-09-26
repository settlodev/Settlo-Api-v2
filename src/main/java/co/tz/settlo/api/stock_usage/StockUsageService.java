package co.tz.settlo.api.stock_usage;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.business.BusinessRepository;
import co.tz.settlo.api.location.Location;
import co.tz.settlo.api.location.LocationRepository;
import co.tz.settlo.api.unit.Unit;
import co.tz.settlo.api.unit.UnitRepository;
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
public class StockUsageService {

    private final StockUsageRepository stockUsageRepository;
    private final UnitRepository unitRepository;
    private final BusinessRepository businessRepository;
    private final LocationRepository locationRepository;

    public StockUsageService(final StockUsageRepository stockUsageRepository,
            final UnitRepository unitRepository, final BusinessRepository businessRepository,
            final LocationRepository locationRepository) {
        this.stockUsageRepository = stockUsageRepository;
        this.unitRepository = unitRepository;
        this.businessRepository = businessRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional(readOnly = true)
    public List<StockUsageDTO> findAll(final UUID locationId) {
        final List<StockUsage> stockUsages = stockUsageRepository.findAllByLocationId(locationId);
        return stockUsages.stream()
                .map(stockUsage -> mapToDTO(stockUsage, new StockUsageDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public StockUsageDTO get(final UUID id) {
        return stockUsageRepository.findById(id)
                .map(stockUsage -> mapToDTO(stockUsage, new StockUsageDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<StockUsageDTO> searchAll(SearchRequest request) {
        SearchSpecification<StockUsage> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<StockUsage> stockUsagePage = stockUsageRepository.findAll(pageable);

        return stockUsagePage.map(stockUsage -> mapToDTO(stockUsage, new StockUsageDTO()));
    }

    @Transactional
    public UUID create(final StockUsageDTO stockUsageDTO) {
        final StockUsage stockUsage = new StockUsage();
        mapToEntity(stockUsageDTO, stockUsage);
        return stockUsageRepository.save(stockUsage).getId();
    }

    @Transactional
    public void update(final UUID id, final StockUsageDTO stockUsageDTO) {
        final StockUsage stockUsage = stockUsageRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(stockUsageDTO, stockUsage);
        stockUsageRepository.save(stockUsage);
    }

    @Transactional
    public void delete(final UUID id) {
        stockUsageRepository.deleteById(id);
    }

    private StockUsageDTO mapToDTO(final StockUsage stockUsage, final StockUsageDTO stockUsageDTO) {
        stockUsageDTO.setId(stockUsage.getId());
        stockUsageDTO.setQuantity(stockUsage.getQuantity());
        stockUsageDTO.setStatus(stockUsage.getStatus());
        stockUsageDTO.setCanDelete(stockUsage.getCanDelete());
        stockUsageDTO.setIsArchived(stockUsage.getIsArchived());
        stockUsageDTO.setItemType(stockUsage.getItemType());
        stockUsageDTO.setItemId(stockUsage.getItemId());
        stockUsageDTO.setUnit(stockUsage.getUnit() == null ? null : stockUsage.getUnit().getId());
        stockUsageDTO.setBusiness(stockUsage.getBusiness() == null ? null : stockUsage.getBusiness().getId());
        stockUsageDTO.setLocation(stockUsage.getLocation() == null ? null : stockUsage.getLocation().getId());
        return stockUsageDTO;
    }

    private StockUsage mapToEntity(final StockUsageDTO stockUsageDTO, final StockUsage stockUsage) {
        stockUsage.setQuantity(stockUsageDTO.getQuantity());
        stockUsage.setStatus(stockUsageDTO.getStatus());
        stockUsage.setCanDelete(stockUsageDTO.getCanDelete());
        stockUsage.setIsArchived(stockUsageDTO.getIsArchived());
        stockUsage.setItemType(stockUsageDTO.getItemType());
        stockUsage.setItemId(stockUsageDTO.getItemId());
        final Unit unit = stockUsageDTO.getUnit() == null ? null : unitRepository.findById(stockUsageDTO.getUnit())
                .orElseThrow(() -> new NotFoundException("unit not found"));
        stockUsage.setUnit(unit);
        final Business business = stockUsageDTO.getBusiness() == null ? null : businessRepository.findById(stockUsageDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        stockUsage.setBusiness(business);
        final Location location = stockUsageDTO.getLocation() == null ? null : locationRepository.findById(stockUsageDTO.getLocation())
                .orElseThrow(() -> new NotFoundException("location not found"));
        stockUsage.setLocation(location);
        return stockUsage;
    }

}
