package co.tz.settlo.api.department;

import co.tz.settlo.api.util.ReferencedException;
import co.tz.settlo.api.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
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
@RequestMapping(value = "/api/departments", produces = MediaType.APPLICATION_JSON_VALUE)
public class DepartmentResource {

    private final DepartmentService departmentService;

    public DepartmentResource(final DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartment(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(departmentService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createDepartment(
            @RequestBody @Valid final DepartmentDTO departmentDTO) {
        final UUID createdId = departmentService.create(departmentDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateDepartment(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final DepartmentDTO departmentDTO) {
        departmentService.update(id, departmentDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDepartment(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = departmentService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
