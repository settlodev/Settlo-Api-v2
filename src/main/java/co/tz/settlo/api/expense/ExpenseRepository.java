package co.tz.settlo.api.expense;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.expense_category.ExpenseCategory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    Expense findFirstByExpenseCategory(ExpenseCategory expenseCategory);

    Expense findFirstByBusiness(Business business);

    boolean existsByNameIgnoreCase(String name);

}
