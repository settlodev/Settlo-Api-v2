package co.tz.settlo.api.supplier;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.business.BusinessRepository;
import co.tz.settlo.api.location.Location;
import co.tz.settlo.api.location.LocationRepository;
import co.tz.settlo.api.stock_intake.StockIntake;
import co.tz.settlo.api.stock_intake.StockIntakeRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final BusinessRepository businessRepository;
    private final LocationRepository locationRepository;
    private final StockIntakeRepository stockIntakeRepository;

    public SupplierService(final SupplierRepository supplierRepository,
            final BusinessRepository businessRepository,
            final LocationRepository locationRepository,
            final StockIntakeRepository stockIntakeRepository) {
        this.supplierRepository = supplierRepository;
        this.businessRepository = businessRepository;
        this.locationRepository = locationRepository;
        this.stockIntakeRepository = stockIntakeRepository;
    }

    public List<SupplierDTO> findAll() {
        final List<Supplier> suppliers = supplierRepository.findAll(Sort.by("id"));
        return suppliers.stream()
                .map(supplier -> mapToDTO(supplier, new SupplierDTO()))
                .toList();
    }

    public SupplierDTO get(final UUID id) {
        return supplierRepository.findById(id)
                .map(supplier -> mapToDTO(supplier, new SupplierDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final SupplierDTO supplierDTO) {
        final Supplier supplier = new Supplier();
        mapToEntity(supplierDTO, supplier);
        return supplierRepository.save(supplier).getId();
    }

    public void update(final UUID id, final SupplierDTO supplierDTO) {
        final Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(supplierDTO, supplier);
        supplierRepository.save(supplier);
    }

    public void delete(final UUID id) {
        supplierRepository.deleteById(id);
    }

    private SupplierDTO mapToDTO(final Supplier supplier, final SupplierDTO supplierDTO) {
        supplierDTO.setId(supplier.getId());
        supplierDTO.setName(supplier.getName());
        supplierDTO.setPhoneNumber(supplier.getPhoneNumber());
        supplierDTO.setEmail(supplier.getEmail());
        supplierDTO.setStatus(supplier.getStatus());
        supplierDTO.setCanDelete(supplier.getCanDelete());
        supplierDTO.setIsArchived(supplier.getIsArchived());
        supplierDTO.setBusiness(supplier.getBusiness() == null ? null : supplier.getBusiness().getId());
        supplierDTO.setLocation(supplier.getLocation() == null ? null : supplier.getLocation().getId());
        return supplierDTO;
    }

    private Supplier mapToEntity(final SupplierDTO supplierDTO, final Supplier supplier) {
        supplier.setName(supplierDTO.getName());
        supplier.setPhoneNumber(supplierDTO.getPhoneNumber());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setStatus(supplierDTO.getStatus());
        supplier.setCanDelete(supplierDTO.getCanDelete());
        supplier.setIsArchived(supplierDTO.getIsArchived());
        final Business business = supplierDTO.getBusiness() == null ? null : businessRepository.findById(supplierDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        supplier.setBusiness(business);
        final Location location = supplierDTO.getLocation() == null ? null : locationRepository.findById(supplierDTO.getLocation())
                .orElseThrow(() -> new NotFoundException("location not found"));
        supplier.setLocation(location);
        return supplier;
    }

    public boolean nameExists(final String name) {
        return supplierRepository.existsByNameIgnoreCase(name);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final StockIntake supplierStockIntake = stockIntakeRepository.findFirstBySupplier(supplier);
        if (supplierStockIntake != null) {
            referencedWarning.setKey("supplier.stockIntake.supplier.referenced");
            referencedWarning.addParam(supplierStockIntake.getId());
            return referencedWarning;
        }
        return null;
    }

}
