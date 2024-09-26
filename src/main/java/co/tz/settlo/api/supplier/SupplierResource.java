package co.tz.settlo.api.supplier;

import co.tz.settlo.api.expense.ExpenseDTO;
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
@RequestMapping(value = "/api/suppliers/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Supplier Endpoints")
public class SupplierResource {

    private final SupplierService supplierService;

    public SupplierResource(final SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    @Operation(summary = "Search all suppliers")
    public Page<SupplierDTO> searchSuppliers(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return supplierService.searchAll(request);
    }

    @GetMapping
    @Operation(summary = "Get all suppliers")
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers(@PathVariable final UUID locationId) {
        return ResponseEntity.ok(supplierService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Supplier")
    public ResponseEntity<SupplierDTO> getSupplier(@PathVariable final UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(supplierService.get(id));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create Supplier")
    public ResponseEntity<UUID> createSupplier(@PathVariable final UUID locationId, @RequestBody @Valid final SupplierDTO supplierDTO) {
        supplierDTO.setLocation(locationId);

        final UUID createdId = supplierService.create(supplierDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Create Supplier")
    public ResponseEntity<UUID> updateSupplier(@PathVariable final UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final SupplierDTO supplierDTO) {
        supplierDTO.setLocation(locationId);

        supplierService.update(id, supplierDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a Supplier")
    public ResponseEntity<Void> deleteSupplier(@PathVariable final UUID locationId, @PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = supplierService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        supplierService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
