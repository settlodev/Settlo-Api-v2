package co.tz.settlo.api.shift;

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
@RequestMapping(value = "/api/shifts", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShiftResource {

    private final ShiftService shiftService;

    public ShiftResource(final ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping
    public ResponseEntity<List<ShiftDTO>> getAllShifts() {
        return ResponseEntity.ok(shiftService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftDTO> getShift(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(shiftService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createShift(@RequestBody @Valid final ShiftDTO shiftDTO) {
        final UUID createdId = shiftService.create(shiftDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateShift(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final ShiftDTO shiftDTO) {
        shiftService.update(id, shiftDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteShift(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = shiftService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        shiftService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
