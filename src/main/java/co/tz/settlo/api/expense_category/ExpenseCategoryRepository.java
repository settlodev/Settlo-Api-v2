package co.tz.settlo.api.expense_category;

import co.tz.settlo.api.business.Business;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, UUID> {

    ExpenseCategory findFirstByBusiness(Business business);

    boolean existsByNameIgnoreCase(String name);

}
