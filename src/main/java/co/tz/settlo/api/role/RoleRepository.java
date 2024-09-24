package co.tz.settlo.api.role;

import co.tz.settlo.api.business.Business;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role findFirstByBusiness(Business business);

}
