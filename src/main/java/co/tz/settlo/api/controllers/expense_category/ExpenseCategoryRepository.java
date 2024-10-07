package co.tz.settlo.api.controllers.expense_category;

import co.tz.settlo.api.controllers.business.Business;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, UUID>, JpaSpecificationExecutor<ExpenseCategory> {

    List<ExpenseCategory> findAllByLocationId(UUID locationId);

    ExpenseCategory findFirstByBusiness(Business business);

    boolean existsByNameIgnoreCase(String name);

}
