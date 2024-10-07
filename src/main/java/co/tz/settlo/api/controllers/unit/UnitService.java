package co.tz.settlo.api.controllers.unit;

import co.tz.settlo.api.controllers.stock_usage.StockUsage;
import co.tz.settlo.api.controllers.stock_usage.StockUsageRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UnitService {

    private final UnitRepository unitRepository;
    private final StockUsageRepository stockUsageRepository;

    public UnitService(final UnitRepository unitRepository,
            final StockUsageRepository stockUsageRepository) {
        this.unitRepository = unitRepository;
        this.stockUsageRepository = stockUsageRepository;
    }

    public List<UnitDTO> findAll() {
        final List<Unit> units = unitRepository.findAll(Sort.by("id"));
        return units.stream()
                .map(unit -> mapToDTO(unit, new UnitDTO()))
                .toList();
    }

    public UnitDTO get(final UUID id) {
        return unitRepository.findById(id)
                .map(unit -> mapToDTO(unit, new UnitDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final UnitDTO unitDTO) {
        final Unit unit = new Unit();
        mapToEntity(unitDTO, unit);
        return unitRepository.save(unit).getId();
    }

    public void update(final UUID id, final UnitDTO unitDTO) {
        final Unit unit = unitRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(unitDTO, unit);
        unitRepository.save(unit);
    }

    public void delete(final UUID id) {
        unitRepository.deleteById(id);
    }

    private UnitDTO mapToDTO(final Unit unit, final UnitDTO unitDTO) {
        unitDTO.setId(unit.getId());
        unitDTO.setName(unit.getName());
        unitDTO.setSymbol(unit.getSymbol());
        unitDTO.setStatus(unit.getStatus());
        unitDTO.setCanDelete(unit.getCanDelete());
        unitDTO.setIsArchived(unit.getIsArchived());
        return unitDTO;
    }

    private Unit mapToEntity(final UnitDTO unitDTO, final Unit unit) {
        unit.setName(unitDTO.getName());
        unit.setSymbol(unitDTO.getSymbol());
        unit.setStatus(unitDTO.getStatus());
        unit.setCanDelete(unitDTO.getCanDelete());
        unit.setIsArchived(unitDTO.getIsArchived());
        return unit;
    }

    public boolean nameExists(final String name) {
        return unitRepository.existsByNameIgnoreCase(name);
    }

    public boolean symbolExists(final String symbol) {
        return unitRepository.existsBySymbolIgnoreCase(symbol);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Unit unit = unitRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final StockUsage unitStockUsage = stockUsageRepository.findFirstByUnit(unit);
        if (unitStockUsage != null) {
            referencedWarning.setKey("unit.stockUsage.unit.referenced");
            referencedWarning.addParam(unitStockUsage.getId());
            return referencedWarning;
        }
        return null;
    }

}
