package co.tz.settlo.api.location;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.business.BusinessRepository;
import co.tz.settlo.api.campaign.Campaign;
import co.tz.settlo.api.campaign.CampaignRepository;
import co.tz.settlo.api.department.Department;
import co.tz.settlo.api.department.DepartmentRepository;
import co.tz.settlo.api.discount.Discount;
import co.tz.settlo.api.discount.DiscountRepository;
import co.tz.settlo.api.location_setting.LocationSetting;
import co.tz.settlo.api.location_setting.LocationSettingRepository;
import co.tz.settlo.api.product.Product;
import co.tz.settlo.api.product.ProductRepository;
import co.tz.settlo.api.reservation.Reservation;
import co.tz.settlo.api.reservation.ReservationRepository;
import co.tz.settlo.api.shift.Shift;
import co.tz.settlo.api.shift.ShiftRepository;
import co.tz.settlo.api.stock.Stock;
import co.tz.settlo.api.stock.StockRepository;
import co.tz.settlo.api.stock_usage.StockUsage;
import co.tz.settlo.api.stock_usage.StockUsageRepository;
import co.tz.settlo.api.supplier.Supplier;
import co.tz.settlo.api.supplier.SupplierRepository;
import co.tz.settlo.api.user.User;
import co.tz.settlo.api.user.UserRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationSettingRepository locationSettingRepository;
    private final BusinessRepository businessRepository;
    private final DepartmentRepository departmentRepository;
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final SupplierRepository supplierRepository;
    private final ShiftRepository shiftRepository;
    private final ReservationRepository reservationRepository;
    private final CampaignRepository campaignRepository;
    private final StockUsageRepository stockUsageRepository;
    private final UserRepository userRepository;

    public LocationService(final LocationRepository locationRepository,
            final LocationSettingRepository locationSettingRepository,
            final BusinessRepository businessRepository,
            final DepartmentRepository departmentRepository,
            final DiscountRepository discountRepository, final ProductRepository productRepository,
            final StockRepository stockRepository, final SupplierRepository supplierRepository,
            final ShiftRepository shiftRepository,
            final ReservationRepository reservationRepository,
            final CampaignRepository campaignRepository,
            final StockUsageRepository stockUsageRepository,
            final UserRepository userRepository
    ) {
        this.locationRepository = locationRepository;
        this.locationSettingRepository = locationSettingRepository;
        this.businessRepository = businessRepository;
        this.departmentRepository = departmentRepository;
        this.discountRepository = discountRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.supplierRepository = supplierRepository;
        this.shiftRepository = shiftRepository;
        this.reservationRepository = reservationRepository;
        this.campaignRepository = campaignRepository;
        this.stockUsageRepository = stockUsageRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<LocationDTO> searchAll(SearchRequest request) {
        SearchSpecification<Location> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Location> locationsPage = locationRepository.findAll(specification, pageable);

        return locationsPage.map(location -> mapToDTO(location, new LocationDTO()));
    }

    @Transactional(readOnly = true)
    public List<LocationDTO> findAll(final UUID businessId) {
        final List<Location> locations = locationRepository.findAllByBusinessId(businessId);
        return locations.stream()
                .map(location -> mapToDTO(location, new LocationDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public LocationDTO get(final UUID id) {
        return locationRepository.findById(id)
                .map(location -> mapToDTO(location, new LocationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public UUID create(final LocationDTO locationDTO) {
        final Location location = new Location();
        mapToEntity(locationDTO, location);

        // ************ Marking location registration complete when we create a location *************** //
        final User user = businessRepository.findById(locationDTO.getBusiness())
                .orElseThrow(NotFoundException::new).getUser();

        user.setIsLocationRegistrationComplete(true);

        userRepository.save(user);
        // ********************************************************************************************** //


        return locationRepository.save(location).getId();
    }

    @Transactional
    public void update(final UUID id, final LocationDTO locationDTO) {
        final Location location = locationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(locationDTO, location);
        locationRepository.save(location);
    }

    @Transactional
    public void delete(final UUID id) {
        locationRepository.deleteById(id);
    }

    private LocationDTO mapToDTO(final Location location, final LocationDTO locationDTO) {
        locationDTO.setId(location.getId());
        locationDTO.setName(location.getName());
        locationDTO.setPhone(location.getPhone());
        locationDTO.setEmail(location.getEmail());
        locationDTO.setCity(location.getCity());
        locationDTO.setRegion(location.getRegion());
        locationDTO.setStreet(location.getStreet());
        locationDTO.setAddress(location.getAddress());
        locationDTO.setDescription(location.getDescription());
        locationDTO.setOpeningTime(location.getOpeningTime());
        locationDTO.setClosingTime(location.getClosingTime());
        locationDTO.setStatus(location.getStatus());
        locationDTO.setIsArchived(location.getIsArchived());
        locationDTO.setCanDelete(location.getCanDelete());
        locationDTO.setSetting(location.getSetting() == null ? null : location.getSetting().getId());
        locationDTO.setBusiness(location.getBusiness() == null ? null : location.getBusiness().getId());
        return locationDTO;
    }

    private Location mapToEntity(final LocationDTO locationDTO, final Location location) {
        location.setName(locationDTO.getName());
        location.setPhone(locationDTO.getPhone());
        location.setEmail(locationDTO.getEmail());
        location.setCity(locationDTO.getCity());
        location.setRegion(locationDTO.getRegion());
        location.setStreet(locationDTO.getStreet());
        location.setAddress(locationDTO.getAddress());
        location.setDescription(locationDTO.getDescription());
        location.setOpeningTime(locationDTO.getOpeningTime());
        location.setClosingTime(locationDTO.getClosingTime());
        location.setStatus(locationDTO.getStatus());
        location.setIsArchived(locationDTO.getIsArchived());
        location.setCanDelete(locationDTO.getCanDelete());
        final LocationSetting setting = locationDTO.getSetting() == null ? null : locationSettingRepository.findById(locationDTO.getSetting())
                .orElseThrow(() -> new NotFoundException("setting not found"));
        location.setSetting(setting);
        final Business business = locationDTO.getBusiness() == null ? null : businessRepository.findById(locationDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        location.setBusiness(business);
        return location;
    }

    public boolean settingExists(final UUID id) {
        return locationRepository.existsBySettingId(id);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Location location = locationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Department locationDepartment = departmentRepository.findFirstByLocation(location);
        if (locationDepartment != null) {
            referencedWarning.setKey("location.department.location.referenced");
            referencedWarning.addParam(locationDepartment.getId());
            return referencedWarning;
        }
        final Discount locationDiscount = discountRepository.findFirstByLocation(location);
        if (locationDiscount != null) {
            referencedWarning.setKey("location.discount.location.referenced");
            referencedWarning.addParam(locationDiscount.getId());
            return referencedWarning;
        }
        final Product locationProduct = productRepository.findFirstByLocation(location);
        if (locationProduct != null) {
            referencedWarning.setKey("location.product.location.referenced");
            referencedWarning.addParam(locationProduct.getId());
            return referencedWarning;
        }
        final Stock locationStock = stockRepository.findFirstByLocation(location);
        if (locationStock != null) {
            referencedWarning.setKey("location.stock.location.referenced");
            referencedWarning.addParam(locationStock.getId());
            return referencedWarning;
        }
        final Supplier locationSupplier = supplierRepository.findFirstByLocation(location);
        if (locationSupplier != null) {
            referencedWarning.setKey("location.supplier.location.referenced");
            referencedWarning.addParam(locationSupplier.getId());
            return referencedWarning;
        }
        final Shift locationShift = shiftRepository.findFirstByLocation(location);
        if (locationShift != null) {
            referencedWarning.setKey("location.shift.location.referenced");
            referencedWarning.addParam(locationShift.getId());
            return referencedWarning;
        }
        final Reservation locationReservation = reservationRepository.findFirstByLocation(location);
        if (locationReservation != null) {
            referencedWarning.setKey("location.reservation.location.referenced");
            referencedWarning.addParam(locationReservation.getId());
            return referencedWarning;
        }
        final Campaign locationCampaign = campaignRepository.findFirstByLocation(location);
        if (locationCampaign != null) {
            referencedWarning.setKey("location.campaign.location.referenced");
            referencedWarning.addParam(locationCampaign.getId());
            return referencedWarning;
        }
        final StockUsage locationStockUsage = stockUsageRepository.findFirstByLocation(location);
        if (locationStockUsage != null) {
            referencedWarning.setKey("location.stockUsage.location.referenced");
            referencedWarning.addParam(locationStockUsage.getId());
            return referencedWarning;
        }
        return null;
    }

}
