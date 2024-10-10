package co.tz.settlo.api.controllers.pending_product_variants;

import co.tz.settlo.api.controllers.pending_product.PendingProduct;
import co.tz.settlo.api.controllers.pending_product.PendingProductRepository;
import co.tz.settlo.api.controllers.product.Product;
import co.tz.settlo.api.controllers.product.ProductRepository;
import co.tz.settlo.api.controllers.tag.Tag;
import co.tz.settlo.api.controllers.tag.TagRepository;
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
public class PendingVariantService {

    private final PendingVariantRepository pendingVariantRepository;
    private final PendingProductRepository pendingProductRepository;
    private final TagRepository tagRepository;

    public PendingVariantService(final PendingVariantRepository pendingVariantRepository,
                                 final ProductRepository productRepository, final TagRepository tagRepository,
                                 final PendingProductRepository pendingProductRepository) {
        this.pendingVariantRepository = pendingVariantRepository;
        this.pendingProductRepository = pendingProductRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional(readOnly = true)
    public List<PendingVariantDTO> findAll(final UUID productId) {
        PendingProduct pendingProduct = pendingProductRepository.findById(productId)
                .orElseThrow(NotFoundException::new);

        final List<PendingVariant> pendingVariants = pendingVariantRepository.findAllByPendingProduct(pendingProduct);
        return pendingVariants.stream()
                .map(pendingVariant -> mapToDTO(pendingVariant, new PendingVariantDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public PendingVariantDTO get(final UUID id) {
        return pendingVariantRepository.findById(id)
                .map(variant -> mapToDTO(variant, new PendingVariantDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<PendingVariantDTO> searchAll(SearchRequest request) {
        SearchSpecification<PendingVariant> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<PendingVariant> variantsPage = pendingVariantRepository.findAll(specification, pageable);

        return variantsPage.map(pendingVariant -> mapToDTO(pendingVariant, new PendingVariantDTO()));
    }

    public UUID create(final PendingVariantDTO pendingVariantDTO) {
        final PendingVariant variant = new PendingVariant();
        mapToEntity(pendingVariantDTO, variant);
        return pendingVariantRepository.save(variant).getId();
    }

    @Transactional
    public void update(final UUID id, final PendingVariantDTO pendingVariantDTO) {
        final PendingVariant variant = pendingVariantRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(pendingVariantDTO, variant);
        pendingVariantRepository.save(variant);
    }

    @Transactional
    public void delete(final UUID id) {
        pendingVariantRepository.deleteById(id);
    }

    private PendingVariantDTO mapToDTO(final PendingVariant pendingVariant, final PendingVariantDTO pendingVariantDTO) {
        pendingVariantDTO.setId(pendingVariant.getId());
        pendingVariantDTO.setName(pendingVariant.getName());
        pendingVariantDTO.setPrice(pendingVariant.getPrice());
        pendingVariantDTO.setCost(pendingVariant.getCost());
        pendingVariantDTO.setQuantity(pendingVariant.getQuantity());
        pendingVariantDTO.setSku(pendingVariant.getSku());
        pendingVariantDTO.setDescription(pendingVariant.getDescription());
        pendingVariantDTO.setImage(pendingVariant.getImage());
        pendingVariantDTO.setStatus(pendingVariant.getStatus());
        pendingVariantDTO.setCanDelete(pendingVariant.getCanDelete());
        pendingVariantDTO.setIsArchived(pendingVariant.getIsArchived());
        pendingVariantDTO.setColor(pendingVariant.getColor());
        pendingVariantDTO.setTaxIncluded(pendingVariant.getTaxIncluded());
        pendingVariantDTO.setTaxAmount(pendingVariant.getTaxAmount());
        pendingVariantDTO.setTaxClass(pendingVariant.getTaxClass());
        pendingVariantDTO.setPendingProduct(pendingVariant.getPendingProduct() == null ? null : pendingVariant.getPendingProduct().getId());
        pendingVariantDTO.setTag(pendingVariant.getTag() == null ? null : pendingVariant.getTag().getId());
        return pendingVariantDTO;
    }

    private PendingVariant mapToEntity(final PendingVariantDTO pendingVariantDTO, final PendingVariant pendingVariant) {
        pendingVariant.setName(pendingVariantDTO.getName());
        pendingVariant.setPrice(pendingVariantDTO.getPrice());
        pendingVariant.setCost(pendingVariantDTO.getCost());
        pendingVariant.setQuantity(pendingVariantDTO.getQuantity());
        pendingVariant.setSku(pendingVariantDTO.getSku());
        pendingVariant.setDescription(pendingVariantDTO.getDescription());
        pendingVariant.setImage(pendingVariantDTO.getImage());
        pendingVariant.setStatus(pendingVariantDTO.getStatus());
        pendingVariant.setCanDelete(pendingVariantDTO.getCanDelete());
        pendingVariant.setIsArchived(pendingVariantDTO.getIsArchived());
        pendingVariant.setColor(pendingVariantDTO.getColor());
        pendingVariant.setTaxIncluded(pendingVariantDTO.getTaxIncluded());
        pendingVariant.setTaxAmount(pendingVariantDTO.getTaxAmount());
        pendingVariant.setTaxClass(pendingVariantDTO.getTaxClass());
        final PendingProduct pendingProduct = pendingVariantDTO.getPendingProduct() == null ? null : pendingProductRepository.findById(pendingVariantDTO.getPendingProduct())
                .orElseThrow(() -> new NotFoundException("product not found"));
        pendingVariant.setPendingProduct(pendingProduct);
        final Tag tag = pendingVariantDTO.getTag() == null ? null : tagRepository.findById(pendingVariantDTO.getTag())
                .orElseThrow(() -> new NotFoundException("tag not found"));
        pendingVariant.setTag(tag);
        return pendingVariant;
    }

    public boolean nameExists(final String name) {
        return pendingVariantRepository.existsByNameIgnoreCase(name);
    }

}
