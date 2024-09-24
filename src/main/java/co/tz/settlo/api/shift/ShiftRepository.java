package co.tz.settlo.api.shift;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.department.Department;
import co.tz.settlo.api.location.Location;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ShiftRepository extends JpaRepository<Shift, UUID> {

    Shift findFirstByBusiness(Business business);

    Shift findFirstByLocation(Location location);

    Shift findFirstByDepartment(Department department);

    boolean existsByNameIgnoreCase(String name);

}
