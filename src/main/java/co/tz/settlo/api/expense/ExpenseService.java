package co.tz.settlo.api.expense;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.business.BusinessRepository;
import co.tz.settlo.api.expense_category.ExpenseCategory;
import co.tz.settlo.api.expense_category.ExpenseCategoryRepository;
import co.tz.settlo.api.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final BusinessRepository businessRepository;

    public ExpenseService(final ExpenseRepository expenseRepository,
            final ExpenseCategoryRepository expenseCategoryRepository,
            final BusinessRepository businessRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.businessRepository = businessRepository;
    }

    public List<ExpenseDTO> findAll() {
        final List<Expense> expenses = expenseRepository.findAll(Sort.by("id"));
        return expenses.stream()
                .map(expense -> mapToDTO(expense, new ExpenseDTO()))
                .toList();
    }

    public ExpenseDTO get(final UUID id) {
        return expenseRepository.findById(id)
                .map(expense -> mapToDTO(expense, new ExpenseDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ExpenseDTO expenseDTO) {
        final Expense expense = new Expense();
        mapToEntity(expenseDTO, expense);
        return expenseRepository.save(expense).getId();
    }

    public void update(final UUID id, final ExpenseDTO expenseDTO) {
        final Expense expense = expenseRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(expenseDTO, expense);
        expenseRepository.save(expense);
    }

    public void delete(final UUID id) {
        expenseRepository.deleteById(id);
    }

    private ExpenseDTO mapToDTO(final Expense expense, final ExpenseDTO expenseDTO) {
        expenseDTO.setId(expense.getId());
        expenseDTO.setName(expense.getName());
        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setStatus(expense.getStatus());
        expenseDTO.setIsArchived(expense.getIsArchived());
        expenseDTO.setCanDelete(expense.getCanDelete());
        expenseDTO.setExpenseCategory(expense.getExpenseCategory() == null ? null : expense.getExpenseCategory().getId());
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
        expense.setBusiness(business);
        return expense;
    }

    public boolean nameExists(final String name) {
        return expenseRepository.existsByNameIgnoreCase(name);
    }

}
