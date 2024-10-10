package co.tz.settlo.api.controllers.pending_product;

import co.tz.settlo.api.controllers.brand.Brand;
import co.tz.settlo.api.controllers.brand.BrandRepository;
import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.business.BusinessRepository;
import co.tz.settlo.api.controllers.category.CategoryRepository;
import co.tz.settlo.api.controllers.department.Department;
import co.tz.settlo.api.controllers.department.DepartmentRepository;
import co.tz.settlo.api.controllers.location.Location;
import co.tz.settlo.api.controllers.location.LocationRepository;
import co.tz.settlo.api.controllers.pending_product_variants.PendingVariantRepository;
import co.tz.settlo.api.controllers.reservation.Reservation;
import co.tz.settlo.api.controllers.reservation.ReservationRepository;
import co.tz.settlo.api.csv_parsers.product_csv_parser.ProductsCsv;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import co.tz.settlo.api.controllers.product_variants.VariantRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.EntityExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PendingProductService {

    private final PendingProductRepository pendingProductRepository;
    private final PendingVariantRepository pendingVariantRepository;
    private final BusinessRepository businessRepository;
    private final LocationRepository locationRepository;
    private final DepartmentRepository departmentRepository;
    private final BrandRepository brandRepository;
    private final ReservationRepository reservationRepository;
    private final CategoryRepository categoryRepository;


    public PendingProductService(final PendingProductRepository pendingProductRepository,
                                 final BusinessRepository businessRepository,
                                 final LocationRepository locationRepository,
                                 final DepartmentRepository departmentRepository, final BrandRepository brandRepository,
                                 final ReservationRepository reservationRepository,
                                 final VariantRepository variantRepository,
                                 final CategoryRepository categoryRepository,
                                 final PendingVariantRepository pendingVariantRepository
    ) {
        this.pendingProductRepository = pendingProductRepository;
        this.businessRepository = businessRepository;
        this.locationRepository = locationRepository;
        this.departmentRepository = departmentRepository;
        this.brandRepository = brandRepository;
        this.reservationRepository = reservationRepository;
        this.categoryRepository =  categoryRepository;
        this.pendingVariantRepository =  pendingVariantRepository;
    }

    @Transactional(readOnly = true)
    public List<PendingProductResponseDTO> findAll(UUID locationId) {
        final List<PendingProduct> products = pendingProductRepository.findAllByLocationId(locationId);
        return products.stream()
                .map(product -> mapToDTO(product, new PendingProductResponseDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public PendingProductResponseDTO get(final UUID id) {
        return pendingProductRepository.findById(id)
                .map(product -> mapToDTO(product, new PendingProductResponseDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<PendingProductResponseDTO> searchAll(SearchRequest request) {
        SearchSpecification<PendingProduct> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<PendingProduct> productsPage = pendingProductRepository.findAll(specification, pageable);

        return productsPage.map(product -> mapToDTO(product, new PendingProductResponseDTO()));
    }

    @Transactional
    public UUID create(final PendingProductDTO productDTO) {
        final PendingProduct product = new PendingProduct();
        mapToEntity(productDTO, product);
        return pendingProductRepository.save(product).getId();
    }

    @Transactional
    public void import_products_csv(final ProductsCsv productsCsv, final UUID locationId) {
        final Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new NotFoundException("location not found"));

        final var savedCategories = productsCsv.getCategoryEntities().stream().map(category -> categoryRepository.findByName(category.getName()).orElseGet( () -> {
            category.setLocation(location);
            return categoryRepository.save(category);
        })).toList();

        // Force persisting all saved categories as we will have to link them with pending products
        categoryRepository.flush();

        for (final var csv_product: productsCsv.getProducts()) {
            final var pendingProduct = csv_product.getPendingProduct();
            final var pendingVariant = csv_product.getPendingVariant();

            final var optionalSavedCategory = savedCategories.stream().filter(savedCategory -> Objects.equals(savedCategory.getName(), csv_product.getCategory().toLowerCase())).findFirst();

            optionalSavedCategory.ifPresentOrElse(pendingProduct::setCategory, () -> { throw new RuntimeException("Did not expect for a category to be absent, this is weird"); });

            pendingProduct.setLocation(location);
            pendingProduct.setBusiness(location.getBusiness());

            final var savedProduct = pendingProductRepository.findByName(pendingProduct.getName()).orElseGet( () -> pendingProductRepository.save(pendingProduct));

            pendingVariant.setPendingProduct(savedProduct);

            if (pendingVariantRepository.existsByNameAndPendingProduct(pendingVariant.getName(), savedProduct)) {
                throw new EntityExistsException(String.format("variant '%s' under product '%s' already exists", pendingVariant.getName(), savedProduct.getName()));
            }

            pendingVariantRepository.save(pendingVariant);

        }
    }

    @Transactional
    public void update(final UUID id, final PendingProductDTO productDTO) {
        final PendingProduct product = pendingProductRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productDTO, product);
        pendingProductRepository.save(product);
    }

    @Transactional
    public void delete(final UUID id) {
        pendingProductRepository.deleteById(id);
    }

    private PendingProductResponseDTO mapToDTO(final PendingProduct product, final PendingProductResponseDTO productDTO) {
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setSlug(product.getSlug());
        productDTO.setSku(product.getSku());
        productDTO.setImage(product.getImage());
        productDTO.setDescription(product.getDescription());
        productDTO.setColor(product.getColor());
        productDTO.setSellOnline(product.getSellOnline());
        productDTO.setStatus(product.getStatus());
        productDTO.setCanDelete(product.getCanDelete());
        productDTO.setIsArchived(product.getIsArchived());
        productDTO.setBusiness(product.getBusiness() == null ? null : product.getBusiness().getId());
        productDTO.setLocation(product.getLocation() == null ? null : product.getLocation().getId());
        productDTO.setDepartment(product.getDepartment() == null ? null : product.getDepartment().getId());
        productDTO.setDepartmentName(product.getDepartment() == null ? null : product.getDepartment().getName());
        productDTO.setBrand(product.getBrand() == null ? null : product.getBrand().getId());
        productDTO.setBrandName(product.getBrand() == null ? null : product.getBrand().getName());
        return productDTO;
    }

    private PendingProduct mapToEntity(final PendingProductDTO productDTO, final PendingProduct product) {
        product.setName(productDTO.getName());
        product.setSlug(productDTO.getSlug());
        product.setSku(productDTO.getSku());
        product.setImage(productDTO.getImage());
        product.setDescription(productDTO.getDescription());
        product.setColor(productDTO.getColor());
        product.setSellOnline(productDTO.getSellOnline());
        product.setStatus(productDTO.getStatus());
        product.setCanDelete(productDTO.getCanDelete());
        product.setIsArchived(productDTO.getIsArchived());
        final Business business = productDTO.getBusiness() == null ? null : businessRepository.findById(productDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        product.setBusiness(business);
        final Location location = productDTO.getLocation() == null ? null : locationRepository.findById(productDTO.getLocation())
                .orElseThrow(() -> new NotFoundException("location not found"));
        product.setLocation(location);
        final Department department = productDTO.getDepartment() == null ? null : departmentRepository.findById(productDTO.getDepartment())
                .orElseThrow(() -> new NotFoundException("department not found"));
        product.setDepartment(department);
        final Brand brand = productDTO.getBrand() == null ? null : brandRepository.findById(productDTO.getBrand())
                .orElseThrow(() -> new NotFoundException("brand not found"));
        product.setBrand(brand);
        return product;
    }

    public boolean nameExists(final String name) {
        return pendingProductRepository.existsByNameIgnoreCase(name);
    }

    public boolean slugExists(final String slug) {
        return pendingProductRepository.existsBySlugIgnoreCase(slug);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final PendingProduct product = pendingProductRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Reservation productReservation = reservationRepository.findFirstByPendingProduct(product);
        if (productReservation != null) {
            referencedWarning.setKey("product.reservation.product.referenced");
            referencedWarning.addParam(productReservation.getId());
            return referencedWarning;
        }
        final var productVariant = pendingVariantRepository.findFirstByPendingProduct(product);
        if (productVariant != null) {
            referencedWarning.setKey("product.variant.product.referenced");
            referencedWarning.addParam(productVariant.getId());
            return referencedWarning;
        }
        return null;
    }

}
