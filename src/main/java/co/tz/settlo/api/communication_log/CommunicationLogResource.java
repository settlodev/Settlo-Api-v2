package co.tz.settlo.api.communication_log;

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
@RequestMapping(value = "/api/communicationLogs", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommunicationLogResource {

    private final CommunicationLogService communicationLogService;

    public CommunicationLogResource(final CommunicationLogService communicationLogService) {
        this.communicationLogService = communicationLogService;
    }

    @GetMapping
    public ResponseEntity<List<CommunicationLogDTO>> getAllCommunicationLogs() {
        return ResponseEntity.ok(communicationLogService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunicationLogDTO> getCommunicationLog(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(communicationLogService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createCommunicationLog(
            @RequestBody @Valid final CommunicationLogDTO communicationLogDTO) {
        final UUID createdId = communicationLogService.create(communicationLogDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateCommunicationLog(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final CommunicationLogDTO communicationLogDTO) {
        communicationLogService.update(id, communicationLogDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCommunicationLog(@PathVariable(name = "id") final UUID id) {
        communicationLogService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
