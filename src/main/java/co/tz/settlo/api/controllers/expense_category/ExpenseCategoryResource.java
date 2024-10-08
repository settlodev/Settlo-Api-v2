package co.tz.settlo.api.controllers.expense_category;

import co.tz.settlo.api.util.ReferencedException;
import co.tz.settlo.api.util.ReferencedWarning;
import co.tz.settlo.api.util.RestApiFilter.FieldType;
import co.tz.settlo.api.util.RestApiFilter.FilterRequest;
import co.tz.settlo.api.util.RestApiFilter.Operator;
import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/expense-categories", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Expense Category Endpoints")
public class ExpenseCategoryResource {

    private final ExpenseCategoryService expenseCategoryService;

    public ExpenseCategoryResource(final ExpenseCategoryService expenseCategoryService) {
        this.expenseCategoryService = expenseCategoryService;
    }

    @GetMapping
    @Operation(summary = "Get all expense categories")
    public ResponseEntity<List<ExpenseCategoryDTO>> getAllExpenseCategories() {
        return ResponseEntity.ok(expenseCategoryService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an expense category")
    public ResponseEntity<ExpenseCategoryDTO> getExpenseCategory(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(expenseCategoryService.get(id));
    }

    @PostMapping
    @Operation(summary = "Search expense categories")
    public Page<ExpenseCategoryDTO> searchExpenseCategories(@RequestBody SearchRequest request) {
        // Enforce Location filter
//        FilterRequest locationFilter = new FilterRequest();
//        locationFilter.setKey("location.id");
//        locationFilter.setOperator(Operator.EQUAL);
//        locationFilter.setFieldType(FieldType.UUID_STRING);

//        request.getFilters().add(locationFilter);

        return expenseCategoryService.searchAll(request);
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create an expense category")
    public ResponseEntity<UUID> createExpenseCategory(
            @RequestBody @Valid final ExpenseCategoryDTO expenseCategoryDTO) {

        final UUID createdId = expenseCategoryService.create(expenseCategoryDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an expense category")
    public ResponseEntity<UUID> updateExpenseCategory(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final ExpenseCategoryDTO expenseCategoryDTO) {

        expenseCategoryService.update(id, expenseCategoryDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete an expense category")
    public ResponseEntity<Void> deleteExpenseCategory(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = expenseCategoryService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        expenseCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
