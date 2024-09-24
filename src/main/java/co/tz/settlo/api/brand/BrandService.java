package co.tz.settlo.api.brand;

import co.tz.settlo.api.product.Product;
import co.tz.settlo.api.product.ProductRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    public BrandService(final BrandRepository brandRepository,
            final ProductRepository productRepository) {
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
    }

    public List<BrandDTO> findAll() {
        final List<Brand> brands = brandRepository.findAll(Sort.by("id"));
        return brands.stream()
                .map(brand -> mapToDTO(brand, new BrandDTO()))
                .toList();
    }

    public BrandDTO get(final UUID id) {
        return brandRepository.findById(id)
                .map(brand -> mapToDTO(brand, new BrandDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final BrandDTO brandDTO) {
        final Brand brand = new Brand();
        mapToEntity(brandDTO, brand);
        return brandRepository.save(brand).getId();
    }

    public void update(final UUID id, final BrandDTO brandDTO) {
        final Brand brand = brandRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(brandDTO, brand);
        brandRepository.save(brand);
    }

    public void delete(final UUID id) {
        brandRepository.deleteById(id);
    }

    private BrandDTO mapToDTO(final Brand brand, final BrandDTO brandDTO) {
        brandDTO.setId(brand.getId());
        brandDTO.setName(brand.getName());
        brandDTO.setStatus(brand.getStatus());
        brandDTO.setCanDelete(brand.getCanDelete());
        brandDTO.setIsArchived(brand.getIsArchived());
        return brandDTO;
    }

    private Brand mapToEntity(final BrandDTO brandDTO, final Brand brand) {
        brand.setName(brandDTO.getName());
        brand.setStatus(brandDTO.getStatus());
        brand.setCanDelete(brandDTO.getCanDelete());
        brand.setIsArchived(brandDTO.getIsArchived());
        return brand;
    }

    public boolean nameExists(final String name) {
        return brandRepository.existsByNameIgnoreCase(name);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Brand brand = brandRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Product brandProduct = productRepository.findFirstByBrand(brand);
        if (brandProduct != null) {
            referencedWarning.setKey("brand.product.brand.referenced");
            referencedWarning.addParam(brandProduct.getId());
            return referencedWarning;
        }
        return null;
    }

}
