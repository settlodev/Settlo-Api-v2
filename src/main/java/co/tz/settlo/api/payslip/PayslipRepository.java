package co.tz.settlo.api.payslip;

import co.tz.settlo.api.salary.Salary;
import co.tz.settlo.api.staff.Staff;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PayslipRepository extends JpaRepository<Payslip, UUID> {

    Payslip findFirstByStaff(Staff staff);

    Payslip findFirstBySalary(Salary salary);

}
