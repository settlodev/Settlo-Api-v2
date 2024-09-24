package co.tz.settlo.api.staff;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.department.Department;
import co.tz.settlo.api.role.Role;
import co.tz.settlo.api.salary.Salary;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StaffRepository extends JpaRepository<Staff, UUID> {

    Staff findFirstByRole(Role role);

    Staff findFirstByBusiness(Business business);

    Staff findFirstByDepartment(Department department);

    Staff findFirstBySalary(Salary salary);

}
