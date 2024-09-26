package co.tz.settlo.api.tag;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface TagRepository extends JpaRepository<Tag, UUID>, JpaSpecificationExecutor<Tag> {

    List<Tag> findAllByLocationId(UUID locationId);

    boolean existsByNameIgnoreCase(String name);

}
