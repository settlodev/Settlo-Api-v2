package co.tz.settlo.api.salary;

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
@RequestMapping(value = "/api/salaries", produces = MediaType.APPLICATION_JSON_VALUE)
public class SalaryResource {

    private final SalaryService salaryService;

    public SalaryResource(final SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @GetMapping
    public ResponseEntity<List<SalaryDTO>> getAllSalaries() {
        return ResponseEntity.ok(salaryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaryDTO> getSalary(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(salaryService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createSalary(@RequestBody @Valid final SalaryDTO salaryDTO) {
        final UUID createdId = salaryService.create(salaryDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateSalary(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final SalaryDTO salaryDTO) {
        salaryService.update(id, salaryDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSalary(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = salaryService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        salaryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
