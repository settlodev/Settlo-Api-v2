package co.tz.settlo.api.department;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.location.Location;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    Department findFirstByLocation(Location location);

    Department findFirstByBusiness(Business business);

}
