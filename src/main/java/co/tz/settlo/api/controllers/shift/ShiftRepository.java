package co.tz.settlo.api.controllers.shift;

import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.department.Department;
import co.tz.settlo.api.controllers.location.Location;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ShiftRepository extends JpaRepository<Shift, UUID> {

    Shift findFirstByBusiness(Business business);

    Shift findFirstByLocation(Location location);

    Shift findFirstByDepartment(Department department);

    boolean existsByNameIgnoreCase(String name);

}
