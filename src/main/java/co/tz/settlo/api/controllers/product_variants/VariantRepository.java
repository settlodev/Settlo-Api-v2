package co.tz.settlo.api.controllers.product_variants;

import co.tz.settlo.api.controllers.product.Product;
import co.tz.settlo.api.controllers.tag.Tag;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface VariantRepository extends JpaRepository<Variant, UUID>, JpaSpecificationExecutor<Variant> {
    List<Variant> findAllByProduct(Product product);

    Variant findFirstByProduct(Product product);

    Variant findFirstByTag(Tag tag);

    boolean existsByNameIgnoreCase(String name);

}
