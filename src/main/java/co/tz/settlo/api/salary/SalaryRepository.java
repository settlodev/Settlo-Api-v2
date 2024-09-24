package co.tz.settlo.api.salary;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SalaryRepository extends JpaRepository<Salary, UUID> {
}
