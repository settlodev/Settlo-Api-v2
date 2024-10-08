package co.tz.settlo.api.controllers.expense;

import co.tz.settlo.api.controllers.business.Business;
import co.tz.settlo.api.controllers.business.BusinessRepository;
import co.tz.settlo.api.controllers.expense_category.ExpenseCategory;
import co.tz.settlo.api.controllers.expense_category.ExpenseCategoryRepository;
import co.tz.settlo.api.controllers.location.Location;
import co.tz.settlo.api.controllers.location.LocationRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final BusinessRepository businessRepository;
    private final LocationRepository locationRepository;

    public ExpenseService(final ExpenseRepository expenseRepository,
            final ExpenseCategoryRepository expenseCategoryRepository,
            final BusinessRepository businessRepository,
                          final LocationRepository locationRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.businessRepository = businessRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponseDTO> findAll(final UUID locationId) {
        final List<Expense> expenses = expenseRepository.findAllByLocationId(locationId);
        return expenses.stream()
                .map(expense -> mapToDTO(expense, new ExpenseResponseDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public ExpenseResponseDTO get(final UUID id) {
        return expenseRepository.findById(id)
                .map(expense -> mapToDTO(expense, new ExpenseResponseDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<ExpenseResponseDTO> searchAll(SearchRequest request) {
        SearchSpecification<Expense> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<Expense> expensesPage = expenseRepository.findAll(specification, pageable);

        return expensesPage.map(expense -> mapToDTO(expense, new ExpenseResponseDTO()));
    }

    @Transactional
    public UUID create(final ExpenseDTO expenseDTO) {
        final Expense expense = new Expense();
        mapToEntity(expenseDTO, expense);
        return expenseRepository.save(expense).getId();
    }

    @Transactional
    public void update(final UUID id, final ExpenseDTO expenseDTO) {
        final Expense expense = expenseRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(expenseDTO, expense);
        expenseRepository.save(expense);
    }

    @Transactional
    public void delete(final UUID id) {
        expenseRepository.deleteById(id);
    }

    private ExpenseResponseDTO mapToDTO(final Expense expense, final ExpenseResponseDTO expenseDTO) {
        expenseDTO.setId(expense.getId());
        expenseDTO.setName(expense.getName());
        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setStatus(expense.getStatus());
        expenseDTO.setIsArchived(expense.getIsArchived());
        expenseDTO.setCanDelete(expense.getCanDelete());
        expenseDTO.setExpenseCategory(expense.getExpenseCategory() == null ? null : expense.getExpenseCategory().getId());
        expenseDTO.setExpenseCategoryName(expense.getExpenseCategory() == null ? null : expense.getExpenseCategory().getName());
        expenseDTO.setBusiness(expense.getBusiness() == null ? null : expense.getBusiness().getId());
        
        return expenseDTO;
    }

    private Expense mapToEntity(final ExpenseDTO expenseDTO, final Expense expense) {
        expense.setName(expenseDTO.getName());
        expense.setAmount(expenseDTO.getAmount());
        expense.setStatus(expenseDTO.getStatus());
        expense.setIsArchived(expenseDTO.getIsArchived());
        expense.setCanDelete(expenseDTO.getCanDelete());
        final ExpenseCategory expenseCategory = expenseDTO.getExpenseCategory() == null ? null : expenseCategoryRepository.findById(expenseDTO.getExpenseCategory())
                .orElseThrow(() -> new NotFoundException("expenseCategory not found"));
        expense.setExpenseCategory(expenseCategory);
        final Business business = expenseDTO.getBusiness() == null ? null : businessRepository.findById(expenseDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        final Location location = expenseDTO.getBusiness() == null ? null : locationRepository.findById(expenseDTO.getLocation())
                .orElseThrow(() -> new NotFoundException("location not found"));

        expense.setLocation(location);
        expense.setBusiness(business);
        return expense;
    }

    public boolean nameExists(final String name) {
        return expenseRepository.existsByNameIgnoreCase(name);
    }

}
