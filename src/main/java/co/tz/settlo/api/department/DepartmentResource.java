package co.tz.settlo.api.department;

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
@RequestMapping(value = "/api/departments/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Departments Endpoints")
public class DepartmentResource {

    private final DepartmentService departmentService;

    public DepartmentResource(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    @Operation(summary = "Get all Departments", description = "Get all the department under certain business Location")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments(@PathVariable final UUID locationId) {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @PostMapping
    @Operation(summary = "Search Departments", description = "Search for a department")
    public Page<DepartmentDTO> searchDepartments(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return departmentService.searchAll(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Department", description = "Get a department using it's ID")
    public ResponseEntity<DepartmentDTO> getDepartment(@PathVariable final UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(departmentService.get(id));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create Department", description = "Create a Department by supplying it as a json body")
    public ResponseEntity<UUID> createDepartment(@PathVariable final UUID locationId,
            @RequestBody @Valid final DepartmentDTO departmentDTO) {
        departmentDTO.setLocation(locationId);

        final UUID createdId = departmentService.create(departmentDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Department", description = "Update a specific department by supplying the new one as json body")
    public ResponseEntity<UUID> updateDepartment(@PathVariable final UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final DepartmentDTO departmentDTO) {
        departmentDTO.setLocation(locationId);

        departmentService.update(id, departmentDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a Department", description = "Delete a department by supplying it's ID")
    public ResponseEntity<Void> deleteDepartment(@PathVariable final UUID locationId, @PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = departmentService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
