package co.tz.settlo.api.variant;

import co.tz.settlo.api.product.Product;
import co.tz.settlo.api.product.ProductRepository;
import co.tz.settlo.api.tag.Tag;
import co.tz.settlo.api.tag.TagRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VariantService {

    private final VariantRepository variantRepository;
    private final ProductRepository productRepository;
    private final TagRepository tagRepository;

    public VariantService(final VariantRepository variantRepository,
            final ProductRepository productRepository, final TagRepository tagRepository) {
        this.variantRepository = variantRepository;
        this.productRepository = productRepository;
        this.tagRepository = tagRepository;
    }

    public List<VariantDTO> findAll() {
        final List<Variant> variants = variantRepository.findAll(Sort.by("id"));
        return variants.stream()
                .map(variant -> mapToDTO(variant, new VariantDTO()))
                .toList();
    }

    public VariantDTO get(final UUID id) {
        return variantRepository.findById(id)
                .map(variant -> mapToDTO(variant, new VariantDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final VariantDTO variantDTO) {
        final Variant variant = new Variant();
        mapToEntity(variantDTO, variant);
        return variantRepository.save(variant).getId();
    }

    public void update(final UUID id, final VariantDTO variantDTO) {
        final Variant variant = variantRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(variantDTO, variant);
        variantRepository.save(variant);
    }

    public void delete(final UUID id) {
        variantRepository.deleteById(id);
    }

    private VariantDTO mapToDTO(final Variant variant, final VariantDTO variantDTO) {
        variantDTO.setId(variant.getId());
        variantDTO.setName(variant.getName());
        variantDTO.setPrice(variant.getPrice());
        variantDTO.setCost(variant.getCost());
        variantDTO.setQuantity(variant.getQuantity());
        variantDTO.setSku(variant.getSku());
        variantDTO.setDescription(variant.getDescription());
        variantDTO.setImage(variant.getImage());
        variantDTO.setStatus(variant.getStatus());
        variantDTO.setCanDelete(variant.getCanDelete());
        variantDTO.setIsArchived(variant.getIsArchived());
        variantDTO.setColor(variant.getColor());
        variantDTO.setTaxIncluded(variant.getTaxIncluded());
        variantDTO.setTaxAmount(variant.getTaxAmount());
        variantDTO.setTaxClass(variant.getTaxClass());
        variantDTO.setProduct(variant.getProduct() == null ? null : variant.getProduct().getId());
        variantDTO.setTag(variant.getTag() == null ? null : variant.getTag().getId());
        return variantDTO;
    }

    private Variant mapToEntity(final VariantDTO variantDTO, final Variant variant) {
        variant.setName(variantDTO.getName());
        variant.setPrice(variantDTO.getPrice());
        variant.setCost(variantDTO.getCost());
        variant.setQuantity(variantDTO.getQuantity());
        variant.setSku(variantDTO.getSku());
        variant.setDescription(variantDTO.getDescription());
        variant.setImage(variantDTO.getImage());
        variant.setStatus(variantDTO.getStatus());
        variant.setCanDelete(variantDTO.getCanDelete());
        variant.setIsArchived(variantDTO.getIsArchived());
        variant.setColor(variantDTO.getColor());
        variant.setTaxIncluded(variantDTO.getTaxIncluded());
        variant.setTaxAmount(variantDTO.getTaxAmount());
        variant.setTaxClass(variantDTO.getTaxClass());
        final Product product = variantDTO.getProduct() == null ? null : productRepository.findById(variantDTO.getProduct())
                .orElseThrow(() -> new NotFoundException("product not found"));
        variant.setProduct(product);
        final Tag tag = variantDTO.getTag() == null ? null : tagRepository.findById(variantDTO.getTag())
                .orElseThrow(() -> new NotFoundException("tag not found"));
        variant.setTag(tag);
        return variant;
    }

    public boolean nameExists(final String name) {
        return variantRepository.existsByNameIgnoreCase(name);
    }

}
