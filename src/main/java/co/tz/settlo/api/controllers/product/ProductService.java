package co.tz.settlo.api.controllers.product;

import co.tz.settlo.api.controllers.brand.Brand;
import co.tz.settlo.api.controllers.brand.BrandRepository;
import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.business.BusinessRepository;
import co.tz.settlo.api.controllers.department.Department;
import co.tz.settlo.api.controllers.department.DepartmentRepository;
import co.tz.settlo.api.controllers.location.Location;
import co.tz.settlo.api.controllers.location.LocationRepository;
import co.tz.settlo.api.controllers.reservation.Reservation;
import co.tz.settlo.api.controllers.reservation.ReservationRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import co.tz.settlo.api.controllers.product_variants.Variant;
import co.tz.settlo.api.controllers.product_variants.VariantRepository;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BusinessRepository businessRepository;
    private final LocationRepository locationRepository;
    private final DepartmentRepository departmentRepository;
    private final BrandRepository brandRepository;
    private final ReservationRepository reservationRepository;
    private final VariantRepository variantRepository;

    public ProductService(final ProductRepository productRepository,
            final BusinessRepository businessRepository,
            final LocationRepository locationRepository,
            final DepartmentRepository departmentRepository, final BrandRepository brandRepository,
            final ReservationRepository reservationRepository,
            final VariantRepository variantRepository) {
        this.productRepository = productRepository;
        this.businessRepository = businessRepository;
        this.locationRepository = locationRepository;
        this.departmentRepository = departmentRepository;
        this.brandRepository = brandRepository;
        this.reservationRepository = reservationRepository;
        this.variantRepository = variantRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll(UUID locationId) {
        final List<Product> products = productRepository.findAllByLocationId(locationId);
        return products.stream()
                .map(product -> mapToDTO(product, new ProductResponseDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO get(final UUID id) {
        return productRepository.findById(id)
                .map(product -> mapToDTO(product, new ProductResponseDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchAll(SearchRequest request) {
        SearchSpecification<Product> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Product> productsPage = productRepository.findAll(specification, pageable);

        return productsPage.map(product -> mapToDTO(product, new ProductResponseDTO()));
    }

    @Transactional
    public UUID create(final ProductDTO productDTO) {
        final Product product = new Product();
        mapToEntity(productDTO, product);
        return productRepository.save(product).getId();
    }

    @Transactional
    public void update(final UUID id, final ProductDTO productDTO) {
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productDTO, product);
        productRepository.save(product);
    }

    @Transactional
    public void delete(final UUID id) {
        productRepository.deleteById(id);
    }

    private ProductResponseDTO mapToDTO(final Product product, final ProductResponseDTO productDTO) {
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

    private Product mapToEntity(final ProductDTO productDTO, final Product product) {
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
        return productRepository.existsByNameIgnoreCase(name);
    }

    public boolean slugExists(final String slug) {
        return productRepository.existsBySlugIgnoreCase(slug);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Reservation productReservation = reservationRepository.findFirstByProduct(product);
        if (productReservation != null) {
            referencedWarning.setKey("product.reservation.product.referenced");
            referencedWarning.addParam(productReservation.getId());
            return referencedWarning;
        }
        final Variant productVariant = variantRepository.findFirstByProduct(product);
        if (productVariant != null) {
            referencedWarning.setKey("product.variant.product.referenced");
            referencedWarning.addParam(productVariant.getId());
            return referencedWarning;
        }
        return null;
    }

}
