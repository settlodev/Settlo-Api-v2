package co.tz.settlo.api.controllers.business;

import co.tz.settlo.api.controllers.campaign.Campaign;
import co.tz.settlo.api.controllers.campaign.CampaignRepository;
import co.tz.settlo.api.controllers.country.Country;
import co.tz.settlo.api.controllers.country.CountryRepository;
import co.tz.settlo.api.controllers.department.Department;
import co.tz.settlo.api.controllers.department.DepartmentRepository;
import co.tz.settlo.api.controllers.expense.Expense;
import co.tz.settlo.api.controllers.expense.ExpenseRepository;
import co.tz.settlo.api.controllers.expense_category.ExpenseCategory;
import co.tz.settlo.api.controllers.expense_category.ExpenseCategoryRepository;
import co.tz.settlo.api.controllers.location.*;
import co.tz.settlo.api.controllers.product.Product;
import co.tz.settlo.api.controllers.product.ProductRepository;
import co.tz.settlo.api.controllers.reservation.Reservation;
import co.tz.settlo.api.controllers.reservation.ReservationRepository;
import co.tz.settlo.api.controllers.role.Role;
import co.tz.settlo.api.controllers.role.RoleRepository;
import co.tz.settlo.api.controllers.sender_id.SenderId;
import co.tz.settlo.api.controllers.sender_id.SenderIdRepository;
import co.tz.settlo.api.controllers.settlement.Settlement;
import co.tz.settlo.api.controllers.settlement.SettlementRepository;
import co.tz.settlo.api.controllers.settlement_account.SettlementAccount;
import co.tz.settlo.api.controllers.settlement_account.SettlementAccountRepository;
import co.tz.settlo.api.controllers.shift.Shift;
import co.tz.settlo.api.controllers.shift.ShiftRepository;
import co.tz.settlo.api.controllers.staff.Staff;
import co.tz.settlo.api.controllers.staff.StaffRepository;
import co.tz.settlo.api.controllers.stock.Stock;
import co.tz.settlo.api.controllers.stock.StockRepository;
import co.tz.settlo.api.controllers.stock_usage.StockUsage;
import co.tz.settlo.api.controllers.stock_usage.StockUsageRepository;
import co.tz.settlo.api.controllers.supplier.Supplier;
import co.tz.settlo.api.controllers.supplier.SupplierRepository;
import co.tz.settlo.api.controllers.user.User;
import co.tz.settlo.api.controllers.user.UserRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final SettlementAccountRepository settlementAccountRepository;
    private final SettlementRepository settlementRepository;
    private final LocationRepository locationRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final StaffRepository staffRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final SupplierRepository supplierRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final ExpenseRepository expenseRepository;
    private final ShiftRepository shiftRepository;
    private final ReservationRepository reservationRepository;
    private final SenderIdRepository senderIdRepository;
    private final CampaignRepository campaignRepository;
    private final StockUsageRepository stockUsageRepository;

    public BusinessService(final BusinessRepository businessRepository,
            final UserRepository userRepository, final CountryRepository countryRepository,
            final SettlementAccountRepository settlementAccountRepository,
            final SettlementRepository settlementRepository,
            final LocationRepository locationRepository,
            final DepartmentRepository departmentRepository, final RoleRepository roleRepository,
            final StaffRepository staffRepository, final ProductRepository productRepository,
            final StockRepository stockRepository, final SupplierRepository supplierRepository,
            final ExpenseCategoryRepository expenseCategoryRepository,
            final ExpenseRepository expenseRepository, final ShiftRepository shiftRepository,
            final ReservationRepository reservationRepository,
            final SenderIdRepository senderIdRepository,
            final CampaignRepository campaignRepository,
            final StockUsageRepository stockUsageRepository) {
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.settlementAccountRepository = settlementAccountRepository;
        this.settlementRepository = settlementRepository;
        this.locationRepository = locationRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
        this.staffRepository = staffRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.supplierRepository = supplierRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.expenseRepository = expenseRepository;
        this.shiftRepository = shiftRepository;
        this.reservationRepository = reservationRepository;
        this.senderIdRepository = senderIdRepository;
        this.campaignRepository = campaignRepository;
        this.stockUsageRepository = stockUsageRepository;
    }

    @Transactional(readOnly = true)
    public List<BusinessResponseDTO> findAll(final UUID userId) {
        final List<Business> businesses = businessRepository.findAllByUserId(userId);

        return businesses.stream()
                .map(business -> mapToDTO(business, new BusinessResponseDTO()))
                .toList();
    }


    @Transactional(readOnly = true)
    public Page<BusinessResponseDTO> searchAll(SearchRequest request) {
        SearchSpecification<Business> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Business> locationsPage = businessRepository.findAll(specification, pageable);

        return locationsPage.map(location -> mapToDTO(location, new BusinessResponseDTO()));
    }

    @Transactional(readOnly = true)
    public BusinessResponseDTO get(final UUID id) {
        return businessRepository.findById(id)
                .map(business -> mapToDTO(business, new BusinessResponseDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public BusinessResponseDTO create(final BusinessCreateDTO businessDTO) {
        final Business business = new Business();
        mapCreateToEntity(businessDTO, business);

        Business savedBusiness = businessRepository.save(business);

        // ************ Marking business registration complete when we create a business *************** //
        final User user = userRepository.findById(businessDTO.getUser())
                .orElseThrow(NotFoundException::new);

        user.setIsBusinessRegistrationComplete(true);

        userRepository.save(user);
        // ********************************************************************************************** //

        return mapToDTO(savedBusiness, new BusinessResponseDTO());
    }

    public void update(final UUID id, final BusinessDTO businessDTO) {
        final Business business = businessRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(businessDTO, business);
        businessRepository.save(business);
    }

    public void delete(final UUID id) {
        businessRepository.deleteById(id);
    }

    private BusinessResponseDTO mapToDTO(final Business business, final BusinessResponseDTO businessDTO) {
        businessDTO.setId(business.getId());
        businessDTO.setPrefix(business.getPrefix());
        businessDTO.setName(business.getName());
        businessDTO.setTax(business.getTax());
        businessDTO.setIdentificationNumber(business.getIdentificationNumber());
        businessDTO.setVrn(business.getVrn());
        businessDTO.setSerial(business.getSerial());
        businessDTO.setUin(business.getUin());
        businessDTO.setReceiptPrefix(business.getReceiptPrefix());
        businessDTO.setReceiptSuffix(business.getReceiptSuffix());
        businessDTO.setBusinessType(business.getBusinessType());
        businessDTO.setSlug(business.getSlug());
        businessDTO.setStoreName(business.getStoreName());
        businessDTO.setImage(business.getImage());
        businessDTO.setReceiptImage(business.getReceiptImage());
        businessDTO.setLogo(business.getLogo());
        businessDTO.setFacebook(business.getFacebook());
        businessDTO.setTwitter(business.getTwitter());
        businessDTO.setInstagram(business.getInstagram());
        businessDTO.setLinkedin(business.getLinkedin());
        businessDTO.setYoutube(business.getYoutube());
        businessDTO.setCertificateOfIncorporation(business.getCertificateOfIncorporation());
        businessDTO.setBusinessIdentificationDocument(business.getBusinessIdentificationDocument());
        businessDTO.setBusinessLicense(business.getBusinessLicense());
        businessDTO.setMemarts(business.getMemarts());
        businessDTO.setNotificationPhone(business.getNotificationPhone());
        businessDTO.setNotificationEmailAddress(business.getNotificationEmailAddress());
        businessDTO.setDescription(business.getDescription());
        businessDTO.setVfdRegistrationState(business.getVfdRegistrationState());
        businessDTO.setWebsite(business.getWebsite());
        businessDTO.setCanDelete(business.getCanDelete());
        businessDTO.setIsArchived(business.getIsArchived());
        businessDTO.setStatus(business.getStatus());
        businessDTO.setUser(business.getUser() == null ? null : business.getUser().getId());
        businessDTO.setCountry(business.getCountry() == null ? null : business.getCountry().getId());
        businessDTO.setCountryName(business.getCountry().getName());
        businessDTO.setTotalLocations(business.getLocations() == null ? 0 : business.getLocations().size());
        businessDTO.setAllLocations(business.getLocations() == null ? Collections.emptyList() :business.getLocations().stream().map(location -> LocationService.mapToDTO(location, new LocationResponseDTO())).toList());
        return businessDTO;
    }

    private Business mapToEntity(final BusinessDTO businessDTO, final Business business) {
        business.setPrefix(businessDTO.getPrefix());
        business.setName(businessDTO.getName());
        business.setTax(businessDTO.getTax());
        business.setIdentificationNumber(businessDTO.getIdentificationNumber());
        business.setVrn(businessDTO.getVrn());
        business.setSerial(businessDTO.getSerial());
        business.setUin(businessDTO.getUin());
        business.setReceiptPrefix(businessDTO.getReceiptPrefix());
        business.setReceiptSuffix(businessDTO.getReceiptSuffix());
        business.setBusinessType(businessDTO.getBusinessType());
        business.setSlug(businessDTO.getSlug());
        business.setStoreName(businessDTO.getStoreName());
        business.setImage(businessDTO.getImage());
        business.setReceiptImage(businessDTO.getReceiptImage());
        business.setLogo(businessDTO.getLogo());
        business.setFacebook(businessDTO.getFacebook());
        business.setTwitter(businessDTO.getTwitter());
        business.setInstagram(businessDTO.getInstagram());
        business.setLinkedin(businessDTO.getLinkedin());
        business.setYoutube(businessDTO.getYoutube());
        business.setCertificateOfIncorporation(businessDTO.getCertificateOfIncorporation());
        business.setBusinessIdentificationDocument(businessDTO.getBusinessIdentificationDocument());
        business.setBusinessLicense(businessDTO.getBusinessLicense());
        business.setMemarts(businessDTO.getMemarts());
        business.setNotificationPhone(businessDTO.getNotificationPhone());
        business.setNotificationEmailAddress(businessDTO.getNotificationEmailAddress());
        business.setDescription(businessDTO.getDescription());
        business.setVfdRegistrationState(businessDTO.getVfdRegistrationState());
        business.setWebsite(businessDTO.getWebsite());
        business.setCanDelete(businessDTO.getCanDelete());
        business.setIsArchived(businessDTO.getIsArchived());
        business.setStatus(businessDTO.getStatus());
        final User user = businessDTO.getUser() == null ? null : userRepository.findById(businessDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        business.setUser(user);
        final Country country = businessDTO.getCountry() == null ? null : countryRepository.findById(businessDTO.getCountry())
                .orElseThrow(() -> new NotFoundException("country not found"));
        business.setCountry(country);
        return business;
    }

    /// Maps the BusinessCreateDTO to an Entity
    private Business mapCreateToEntity(final BusinessCreateDTO businessDTO, final Business business) {
        Random randomGen = new Random();

        business.setPrefix(String.valueOf(randomGen.nextInt() + 1000));
        business.setName(businessDTO.getName());
        business.setTax(0.0);
//        business.setIdentificationNumber(businessDTO.getIdentificationNumber());
//        business.setVrn(businessDTO.getVrn());
//        business.setSerial(businessDTO.getSerial());
//        business.setUin(businessDTO.getUin());
//        business.setReceiptPrefix(businessDTO.getReceiptPrefix());
//        business.setReceiptSuffix(businessDTO.getReceiptSuffix());
        business.setBusinessType(businessDTO.getBusinessType());
        business.setSlug(businessDTO.getName().replace(" ", "-"));
//        business.setStoreName(businessDTO.getStoreName());
//        business.setImage(businessDTO.getImage());
//        business.setReceiptImage(businessDTO.getReceiptImage());
//        business.setLogo(businessDTO.getLogo());
//        business.setFacebook(businessDTO.getFacebook());
//        business.setTwitter(businessDTO.getTwitter());
//        business.setInstagram(businessDTO.getInstagram());
//        business.setLinkedin(businessDTO.getLinkedin());
//        business.setYoutube(businessDTO.getYoutube());
//        business.setCertificateOfIncorporation(businessDTO.getCertificateOfIncorporation());
//        business.setBusinessIdentificationDocument(businessDTO.getBusinessIdentificationDocument());
//        business.setBusinessLicense(businessDTO.getBusinessLicense());
//        business.setMemarts(businessDTO.getMemarts());
//        business.setNotificationPhone(businessDTO.getNotificationPhone());
        business.setNotificationEmailAddress("");
        business.setDescription(businessDTO.getDescription());
        business.setVfdRegistrationState(false);
//        business.setWebsite(businessDTO.getWebsite());
        business.setCanDelete(true);
        business.setIsArchived(false);
        business.setStatus(true);

        final User user = businessDTO.getUser() == null ? null : userRepository.findById(businessDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        business.setUser(user);
        final Country country = businessDTO.getCountry() == null ? null : countryRepository.findById(businessDTO.getCountry())
                .orElseThrow(() -> new NotFoundException("country not found"));
        business.setCountry(country);
        return business;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Business business = businessRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final SettlementAccount businessSettlementAccount = settlementAccountRepository.findFirstByBusiness(business);
        if (businessSettlementAccount != null) {
            referencedWarning.setKey("business.settlementAccount.business.referenced");
            referencedWarning.addParam(businessSettlementAccount.getId());
            return referencedWarning;
        }
        final Settlement businessSettlement = settlementRepository.findFirstByBusiness(business);
        if (businessSettlement != null) {
            referencedWarning.setKey("business.settlement.business.referenced");
            referencedWarning.addParam(businessSettlement.getId());
            return referencedWarning;
        }
        final Location businessLocation = locationRepository.findFirstByBusiness(business);
        if (businessLocation != null) {
            referencedWarning.setKey("business.location.business.referenced");
            referencedWarning.addParam(businessLocation.getId());
            return referencedWarning;
        }
        final Department businessDepartment = departmentRepository.findFirstByBusiness(business);
        if (businessDepartment != null) {
            referencedWarning.setKey("business.department.business.referenced");
            referencedWarning.addParam(businessDepartment.getId());
            return referencedWarning;
        }
        final Role businessRole = roleRepository.findFirstByBusiness(business);
        if (businessRole != null) {
            referencedWarning.setKey("business.role.business.referenced");
            referencedWarning.addParam(businessRole.getId());
            return referencedWarning;
        }
        final Staff businessStaff = staffRepository.findFirstByBusiness(business);
        if (businessStaff != null) {
            referencedWarning.setKey("business.staff.business.referenced");
            referencedWarning.addParam(businessStaff.getId());
            return referencedWarning;
        }
        final Product businessProduct = productRepository.findFirstByBusiness(business);
        if (businessProduct != null) {
            referencedWarning.setKey("business.product.business.referenced");
            referencedWarning.addParam(businessProduct.getId());
            return referencedWarning;
        }
        final Stock businessStock = stockRepository.findFirstByBusiness(business);
        if (businessStock != null) {
            referencedWarning.setKey("business.stock.business.referenced");
            referencedWarning.addParam(businessStock.getId());
            return referencedWarning;
        }
        final Supplier businessSupplier = supplierRepository.findFirstByBusiness(business);
        if (businessSupplier != null) {
            referencedWarning.setKey("business.supplier.business.referenced");
            referencedWarning.addParam(businessSupplier.getId());
            return referencedWarning;
        }
        final ExpenseCategory businessExpenseCategory = expenseCategoryRepository.findFirstByBusiness(business);
        if (businessExpenseCategory != null) {
            referencedWarning.setKey("business.expenseCategory.business.referenced");
            referencedWarning.addParam(businessExpenseCategory.getId());
            return referencedWarning;
        }
        final Expense businessExpense = expenseRepository.findFirstByBusiness(business);
        if (businessExpense != null) {
            referencedWarning.setKey("business.expense.business.referenced");
            referencedWarning.addParam(businessExpense.getId());
            return referencedWarning;
        }
        final Shift businessShift = shiftRepository.findFirstByBusiness(business);
        if (businessShift != null) {
            referencedWarning.setKey("business.shift.business.referenced");
            referencedWarning.addParam(businessShift.getId());
            return referencedWarning;
        }
        final Reservation businessReservation = reservationRepository.findFirstByBusiness(business);
        if (businessReservation != null) {
            referencedWarning.setKey("business.reservation.business.referenced");
            referencedWarning.addParam(businessReservation.getId());
            return referencedWarning;
        }
        final SenderId businessSenderId = senderIdRepository.findFirstByBusiness(business);
        if (businessSenderId != null) {
            referencedWarning.setKey("business.senderId.business.referenced");
            referencedWarning.addParam(businessSenderId.getId());
            return referencedWarning;
        }
        final Campaign businessCampaign = campaignRepository.findFirstByBusiness(business);
        if (businessCampaign != null) {
            referencedWarning.setKey("business.campaign.business.referenced");
            referencedWarning.addParam(businessCampaign.getId());
            return referencedWarning;
        }
        final StockUsage businessStockUsage = stockUsageRepository.findFirstByBusiness(business);
        if (businessStockUsage != null) {
            referencedWarning.setKey("business.stockUsage.business.referenced");
            referencedWarning.addParam(businessStockUsage.getId());
            return referencedWarning;
        }
        return null;
    }

}
