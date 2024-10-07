package co.tz.settlo.api.controllers.payslip;

import co.tz.settlo.api.controllers.salary.Salary;
import co.tz.settlo.api.controllers.staff.Staff;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PayslipRepository extends JpaRepository<Payslip, UUID>, JpaSpecificationExecutor<Payslip> {

    List<Payslip> findAllByLocationId(UUID locationId);

    Payslip findFirstByStaff(Staff staff);

    Payslip findFirstBySalary(Salary salary);

}
