package co.tz.settlo.api.controllers.salary;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SalaryRepository extends JpaRepository<Salary, UUID> {
}
