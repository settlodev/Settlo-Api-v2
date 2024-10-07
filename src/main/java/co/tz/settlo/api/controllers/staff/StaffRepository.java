package co.tz.settlo.api.controllers.staff;

import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.department.Department;
import co.tz.settlo.api.controllers.role.Role;
import co.tz.settlo.api.controllers.salary.Salary;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface StaffRepository extends JpaRepository<Staff, UUID>, JpaSpecificationExecutor<Staff> {

    List<Staff> findAllByLocationId(UUID locationId);

    Staff findFirstByRole(Role role);

    Staff findFirstByBusiness(Business business);

    Staff findFirstByDepartment(Department department);

    Staff findFirstBySalary(Salary salary);

}
