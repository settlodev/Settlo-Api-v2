package co.tz.settlo.api.unit;

import co.tz.settlo.api.util.ReferencedException;
import co.tz.settlo.api.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping(value = "/api/units", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Unit Endpoints")
public class UnitResource {

    private final UnitService unitService;

    public UnitResource(final UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping
    @Operation(summary = "Get all Units")
    public ResponseEntity<List<UnitDTO>> getAllUnits() {
        return ResponseEntity.ok(unitService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Unit")
    public ResponseEntity<UnitDTO> getUnit(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(unitService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a Unit")
    public ResponseEntity<UUID> createUnit(@RequestBody @Valid final UnitDTO unitDTO) {
        final UUID createdId = unitService.create(unitDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Unit")
    public ResponseEntity<UUID> updateUnit(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final UnitDTO unitDTO) {
        unitService.update(id, unitDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a Unit")
    public ResponseEntity<Void> deleteUnit(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = unitService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        unitService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
