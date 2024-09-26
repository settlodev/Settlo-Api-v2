package co.tz.settlo.api.staff;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.department.Department;
import co.tz.settlo.api.role.Role;
import co.tz.settlo.api.salary.Salary;

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
