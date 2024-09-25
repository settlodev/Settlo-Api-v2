package co.tz.settlo.api.expense_category;

import co.tz.settlo.api.business.Business;
import co.tz.settlo.api.business.BusinessRepository;
import co.tz.settlo.api.expense.Expense;
import co.tz.settlo.api.expense.ExpenseRepository;
import co.tz.settlo.api.util.NotFoundException;
import co.tz.settlo.api.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;

import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import co.tz.settlo.api.util.RestApiFilter.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ExpenseCategoryService {

    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final BusinessRepository businessRepository;
    private final ExpenseRepository expenseRepository;

    public ExpenseCategoryService(final ExpenseCategoryRepository expenseCategoryRepository,
            final BusinessRepository businessRepository,
            final ExpenseRepository expenseRepository) {
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.businessRepository = businessRepository;
        this.expenseRepository = expenseRepository;
    }

    public List<ExpenseCategoryDTO> findAll(final UUID locationId) {
        final List<ExpenseCategory> expenseCategories = expenseCategoryRepository.findAllByLocationId(locationId);
        return expenseCategories.stream()
                .map(expenseCategory -> mapToDTO(expenseCategory, new ExpenseCategoryDTO()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ExpenseCategoryDTO> searchAll(SearchRequest request) {
        SearchSpecification<ExpenseCategory> specification = new SearchSpecification<>(request);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<ExpenseCategory> expenseCategoryPage = expenseCategoryRepository.findAll(specification, pageable);

        return expenseCategoryPage.map(expense -> mapToDTO(expense, new ExpenseCategoryDTO()));
    }

    public ExpenseCategoryDTO get(final UUID id) {
        return expenseCategoryRepository.findById(id)
                .map(expenseCategory -> mapToDTO(expenseCategory, new ExpenseCategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ExpenseCategoryDTO expenseCategoryDTO) {
        final ExpenseCategory expenseCategory = new ExpenseCategory();
        mapToEntity(expenseCategoryDTO, expenseCategory);
        return expenseCategoryRepository.save(expenseCategory).getId();
    }

    public void update(final UUID id, final ExpenseCategoryDTO expenseCategoryDTO) {
        final ExpenseCategory expenseCategory = expenseCategoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(expenseCategoryDTO, expenseCategory);
        expenseCategoryRepository.save(expenseCategory);
    }

    public void delete(final UUID id) {
        expenseCategoryRepository.deleteById(id);
    }

    private ExpenseCategoryDTO mapToDTO(final ExpenseCategory expenseCategory,
            final ExpenseCategoryDTO expenseCategoryDTO) {
        expenseCategoryDTO.setId(expenseCategory.getId());
        expenseCategoryDTO.setName(expenseCategory.getName());
        expenseCategoryDTO.setStatus(expenseCategory.getStatus());
        expenseCategoryDTO.setIsArchived(expenseCategory.getIsArchived());
        expenseCategoryDTO.setCanDelete(expenseCategory.getCanDelete());
        expenseCategoryDTO.setBusiness(expenseCategory.getBusiness() == null ? null : expenseCategory.getBusiness().getId());
        return expenseCategoryDTO;
    }

    private ExpenseCategory mapToEntity(final ExpenseCategoryDTO expenseCategoryDTO,
            final ExpenseCategory expenseCategory) {
        expenseCategory.setName(expenseCategoryDTO.getName());
        expenseCategory.setStatus(expenseCategoryDTO.getStatus());
        expenseCategory.setIsArchived(expenseCategoryDTO.getIsArchived());
        expenseCategory.setCanDelete(expenseCategoryDTO.getCanDelete());
        final Business business = expenseCategoryDTO.getBusiness() == null ? null : businessRepository.findById(expenseCategoryDTO.getBusiness())
                .orElseThrow(() -> new NotFoundException("business not found"));
        expenseCategory.setBusiness(business);
        return expenseCategory;
    }

    public boolean nameExists(final String name) {
        return expenseCategoryRepository.existsByNameIgnoreCase(name);
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final ExpenseCategory expenseCategory = expenseCategoryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Expense expenseCategoryExpense = expenseRepository.findFirstByExpenseCategory(expenseCategory);
        if (expenseCategoryExpense != null) {
            referencedWarning.setKey("expenseCategory.expense.expenseCategory.referenced");
            referencedWarning.addParam(expenseCategoryExpense.getId());
            return referencedWarning;
        }
        return null;
    }

}
