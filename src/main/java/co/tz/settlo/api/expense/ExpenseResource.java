package co.tz.settlo.api.expense;

import co.tz.settlo.api.reservation.ReservationDTO;
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
@RequestMapping(value = "/api/expenses/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Expenses Endpoints")
public class ExpenseResource {

    private final ExpenseService expenseService;

    public ExpenseResource(final ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @Operation(summary = "Search Expenses")
    public Page<ExpenseDTO> searchExpenses(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location.id");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.UUID_STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return expenseService.searchAll(request);
    }

    @GetMapping
    @Operation(summary = "Get all expenses")
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses(@PathVariable UUID locationId) {
        return ResponseEntity.ok(expenseService.findAll(locationId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an expenses")
    public ResponseEntity<ExpenseDTO> getExpense(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(expenseService.get(id));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create an expense")
    public ResponseEntity<UUID> createExpense(@PathVariable UUID locationId,@RequestBody @Valid final ExpenseDTO expenseDTO) {
        expenseDTO.setLocation(locationId);
        final UUID createdId = expenseService.create(expenseDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an expense")
    public ResponseEntity<UUID> updateExpense(@PathVariable UUID locationId,@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final ExpenseDTO expenseDTO) {
        expenseDTO.setLocation(locationId);
        expenseService.update(id, expenseDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete an expense")
    public ResponseEntity<Void> deleteExpense(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
