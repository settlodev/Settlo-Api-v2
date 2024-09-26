package co.tz.settlo.api.product_variants;

import co.tz.settlo.api.product.Product;
import co.tz.settlo.api.tag.Tag;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VariantRepository extends JpaRepository<Variant, UUID> {

    Variant findFirstByProduct(Product product);

    Variant findFirstByTag(Tag tag);

    boolean existsByNameIgnoreCase(String name);

}
