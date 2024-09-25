package co.tz.settlo.api.discount;

import co.tz.settlo.api.category.Category;
import co.tz.settlo.api.customer.Customer;
import co.tz.settlo.api.department.Department;
import co.tz.settlo.api.expense.Expense;
import co.tz.settlo.api.location.Location;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface  DiscountRepository  extends JpaRepository<Discount, UUID>, JpaSpecificationExecutor<Discount> {
    List<Discount> findAllByLocationId(UUID locationId);

    Discount findFirstByDepartment(Department department);

    Discount findFirstByCustomer(Customer customer);

    Discount findFirstByLocation(Location location);

    Discount findFirstByCategory(Category category);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByDiscountCodeIgnoreCase(String discountCode);

}
