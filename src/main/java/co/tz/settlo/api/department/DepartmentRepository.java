package co.tz.settlo.api.department;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.expense.Expense;
import co.tz.settlo.api.location.Location;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface  DepartmentRepository  extends JpaRepository<Department, UUID>, JpaSpecificationExecutor<Department> {

    List<Department> findAllByLocationId(UUID locationId);

    Department findFirstByLocation(Location location);

    Department findFirstByBusiness(Business business);

}
