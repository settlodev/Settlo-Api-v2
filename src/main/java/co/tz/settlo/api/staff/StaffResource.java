package co.tz.settlo.api.staff;

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
@RequestMapping(value = "/api/staffs", produces = MediaType.APPLICATION_JSON_VALUE)
public class StaffResource {

    private final StaffService staffService;

    public StaffResource(final StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public ResponseEntity<List<StaffDTO>> getAllStaffs() {
        return ResponseEntity.ok(staffService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffDTO> getStaff(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(staffService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createStaff(@RequestBody @Valid final StaffDTO staffDTO) {
        final UUID createdId = staffService.create(staffDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateStaff(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final StaffDTO staffDTO) {
        staffService.update(id, staffDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteStaff(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = staffService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        staffService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
