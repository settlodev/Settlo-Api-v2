package co.tz.settlo.api.controllers.category;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CategoryRepository extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {
    List<Category> findAllByLocationId(UUID locationId);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByParentId(UUID parentId);

}
