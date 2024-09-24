package co.tz.settlo.api.shift_log;

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
@RequestMapping(value = "/api/shiftLogs", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShiftLogResource {

    private final ShiftLogService shiftLogService;

    public ShiftLogResource(final ShiftLogService shiftLogService) {
        this.shiftLogService = shiftLogService;
    }

    @GetMapping
    public ResponseEntity<List<ShiftLogDTO>> getAllShiftLogs() {
        return ResponseEntity.ok(shiftLogService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShiftLogDTO> getShiftLog(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(shiftLogService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createShiftLog(@RequestBody @Valid final ShiftLogDTO shiftLogDTO) {
        final UUID createdId = shiftLogService.create(shiftLogDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateShiftLog(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final ShiftLogDTO shiftLogDTO) {
        shiftLogService.update(id, shiftLogDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteShiftLog(@PathVariable(name = "id") final UUID id) {
        shiftLogService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
