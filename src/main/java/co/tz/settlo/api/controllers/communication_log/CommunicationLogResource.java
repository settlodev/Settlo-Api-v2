package co.tz.settlo.api.controllers.communication_log;

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
@RequestMapping(value = "/api/communication-logs/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Communication log Endpoints")
public class CommunicationLogResource {

    private final CommunicationLogService communicationLogService;

    public CommunicationLogResource(final CommunicationLogService communicationLogService) {
        this.communicationLogService = communicationLogService;
    }

    @GetMapping
    @Operation(summary = "Get all communication log")
    public ResponseEntity<List<CommunicationLogDTO>> getAllCommunicationLogs(@PathVariable UUID locationId) {
        return ResponseEntity.ok(communicationLogService.findAll(locationId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a communication log", description = "Get a communication log by specifying it's ID")
    public ResponseEntity<CommunicationLogDTO> getCommunicationLog( @PathVariable UUID locationId,
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(communicationLogService.get(id));
    }

    @PostMapping
    @Operation(summary = "Search communication logs")
    public Page<CommunicationLogDTO> searchCommunicationLogs(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location.id");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.UUID_STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return communicationLogService.searchAll(request);
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create communication log")
    public ResponseEntity<UUID> createCommunicationLog( @PathVariable UUID locationId,
            @RequestBody @Valid final CommunicationLogDTO communicationLogDTO) {

        communicationLogDTO.setLocation(locationId);

        final UUID createdId = communicationLogService.create(communicationLogDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update communication log")
    public ResponseEntity<UUID> updateCommunicationLog(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final CommunicationLogDTO communicationLogDTO) {

        communicationLogDTO.setLocation(locationId);

        communicationLogService.update(id, communicationLogDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a communication log")
    public ResponseEntity<Void> deleteCommunicationLog(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        communicationLogService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
