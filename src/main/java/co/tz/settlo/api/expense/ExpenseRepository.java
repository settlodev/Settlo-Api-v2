package co.tz.settlo.api.expense;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.expense_category.ExpenseCategory;

import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;




public interface  ExpenseRepository extends JpaRepository<Expense, UUID>, JpaSpecificationExecutor<Expense> {

    List<Expense> findAllByLocationId(UUID locationId);

    Expense findFirstByExpenseCategory(ExpenseCategory expenseCategory);

    Expense findFirstByBusiness(Business business);

    boolean existsByNameIgnoreCase(String name);

}
