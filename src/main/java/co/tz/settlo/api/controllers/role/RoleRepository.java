package co.tz.settlo.api.controllers.role;

import co.tz.settlo.api.controllers.business.Business;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface RoleRepository extends JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role> {
    List<Role> findAllByBusinessId(UUID businessId);

    Role findFirstByBusiness(Business business);

    Optional<Role> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
