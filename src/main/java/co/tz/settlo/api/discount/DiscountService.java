package co.tz.settlo.api.discount;

import co.tz.settlo.api.category.Category;
import co.tz.settlo.api.category.CategoryRepository;
import co.tz.settlo.api.customer.Customer;
import co.tz.settlo.api.customer.CustomerRepository;
import co.tz.settlo.api.department.Department;
import co.tz.settlo.api.department.DepartmentRepository;
import co.tz.settlo.api.expense.Expense;
import co.tz.settlo.api.expense.ExpenseDTO;
import co.tz.settlo.api.location.Location;
import co.tz.settlo.api.location.LocationRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final DepartmentRepository departmentRepository;
    private final CustomerRepository customerRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;

    public DiscountService(final DiscountRepository discountRepository,
            final DepartmentRepository departmentRepository,
            final CustomerRepository customerRepository,
            final LocationRepository locationRepository,
            final CategoryRepository categoryRepository) {
        this.discountRepository = discountRepository;
        this.departmentRepository = departmentRepository;
        this.customerRepository = customerRepository;
        this.locationRepository = locationRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<DiscountResponseDTO> searchAll(SearchRequest request) {
        SearchSpecification<Discount> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Discount> expensesPage = discountRepository.findAll(specification, pageable);

        return expensesPage.map(discount -> mapToDTO(discount, new DiscountResponseDTO()));
    }

    @Transactional(readOnly = true)
    public List<DiscountResponseDTO> findAll() {
        final List<Discount> discounts = discountRepository.findAll(Sort.by("id"));
        return discounts.stream()
                .map(discount -> mapToDTO(discount, new DiscountResponseDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public DiscountResponseDTO get(final UUID id) {
        return discountRepository.findById(id)
                .map(discount -> mapToDTO(discount, new DiscountResponseDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public UUID create(final DiscountDTO discountDTO) {
        final Discount discount = new Discount();
        mapToEntity(discountDTO, discount);
        return discountRepository.save(discount).getId();
    }

    @Transactional
    public void update(final UUID id, final DiscountDTO discountDTO) {
        final Discount discount = discountRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(discountDTO, discount);
        discountRepository.save(discount);
    }

    @Transactional
    public void delete(final UUID id) {
        discountRepository.deleteById(id);
    }

    private DiscountResponseDTO mapToDTO(final Discount discount, final DiscountResponseDTO discountDTO) {
        discountDTO.setId(discount.getId());
        discountDTO.setName(discount.getName());
        discountDTO.setDiscountValue(discount.getDiscountValue());
        discountDTO.setValidFrom(discount.getValidFrom());
        discountDTO.setValidTo(discount.getValidTo());
        discountDTO.setDiscountCode(discount.getDiscountCode());
        discountDTO.setMinimumSpend(discount.getMinimumSpend());
        discountDTO.setDiscountType(discount.getDiscountType());
        discountDTO.setUsageLimit(discount.getUsageLimit());
        discountDTO.setActivations(discount.getActivations());
        discountDTO.setStatus(discount.getStatus());
        discountDTO.setIsArchived(discount.getIsArchived());
        discountDTO.setCanDelete(discount.getCanDelete());
        discountDTO.setDepartment(discount.getDepartment() == null ? null : discount.getDepartment().getId());
        discountDTO.setCustomer(discount.getCustomer() == null ? null : discount.getCustomer().getId());
        discountDTO.setLocation(discount.getLocation() == null ? null : discount.getLocation().getId());
        discountDTO.setCategory(discount.getCategory() == null ? null : discount.getCategory().getId());
        return discountDTO;
    }

    private Discount mapToEntity(final DiscountDTO discountDTO, final Discount discount) {
        discount.setName(discountDTO.getName());
        discount.setDiscountValue(discountDTO.getDiscountValue());
        discount.setValidFrom(discountDTO.getValidFrom());
        discount.setValidTo(discountDTO.getValidTo());
        discount.setDiscountCode(discountDTO.getDiscountCode());
        discount.setMinimumSpend(discountDTO.getMinimumSpend());
        discount.setDiscountType(discountDTO.getDiscountType());
        discount.setUsageLimit(discountDTO.getUsageLimit());
        discount.setActivations(discountDTO.getActivations());
        discount.setStatus(discountDTO.getStatus());
        discount.setIsArchived(discountDTO.getIsArchived());
        discount.setCanDelete(discountDTO.getCanDelete());
        final Department department = discountDTO.getDepartment() == null ? null : departmentRepository.findById(discountDTO.getDepartment())
                .orElseThrow(() -> new NotFoundException("department not found"));
        discount.setDepartment(department);
        final Customer customer = discountDTO.getCustomer() == null ? null : customerRepository.findById(discountDTO.getCustomer())
                .orElseThrow(() -> new NotFoundException("customer not found"));
        discount.setCustomer(customer);
        final Location location = discountDTO.getLocation() == null ? null : locationRepository.findById(discountDTO.getLocation())
                .orElseThrow(() -> new NotFoundException("location not found"));
        discount.setLocation(location);
        final Category category = discountDTO.getCategory() == null ? null : categoryRepository.findById(discountDTO.getCategory())
                .orElseThrow(() -> new NotFoundException("category not found"));
        discount.setCategory(category);
        return discount;
    }

    public boolean nameExists(final String name) {
        return discountRepository.existsByNameIgnoreCase(name);
    }

    public boolean discountCodeExists(final String discountCode) {
        return discountRepository.existsByDiscountCodeIgnoreCase(discountCode);
    }

}
