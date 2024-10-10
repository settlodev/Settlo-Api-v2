package co.tz.settlo.api.controllers.pending_product_variants;

import co.tz.settlo.api.controllers.pending_product.PendingProduct;
import co.tz.settlo.api.controllers.product.Product;
import co.tz.settlo.api.controllers.tag.Tag;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PendingVariantRepository extends JpaRepository<PendingVariant, UUID>, JpaSpecificationExecutor<PendingVariant> {
    List<PendingVariant> findAllByPendingProduct(PendingProduct pendingProduct);

    PendingVariant findFirstByPendingProduct(PendingProduct pendingProduct);

    boolean existsByNameAndPendingProduct(String variantName, PendingProduct pendingProduct);

    PendingVariant findFirstByTag(Tag tag);

    boolean existsByNameIgnoreCase(String name);

}
