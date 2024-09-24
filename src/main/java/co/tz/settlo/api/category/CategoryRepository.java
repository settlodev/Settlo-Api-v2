package co.tz.settlo.api.category;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByParentId(UUID parentId);

}
