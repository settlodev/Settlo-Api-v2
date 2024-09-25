package co.tz.settlo.api.communication_log;

import co.tz.settlo.api.category.CategoryDTO;
import co.tz.settlo.api.util.RestApiFilter.FieldType;
import co.tz.settlo.api.util.RestApiFilter.FilterRequest;
import co.tz.settlo.api.util.RestApiFilter.Operator;
import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
public class CommunicationLogResource {

    private final CommunicationLogService communicationLogService;

    public CommunicationLogResource(final CommunicationLogService communicationLogService) {
        this.communicationLogService = communicationLogService;
    }

    @GetMapping
    public ResponseEntity<List<CommunicationLogDTO>> getAllCommunicationLogs(@PathVariable UUID locationId) {
        return ResponseEntity.ok(communicationLogService.findAll(locationId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunicationLogDTO> getCommunicationLog( @PathVariable UUID locationId,
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(communicationLogService.get(id));
    }

    @PostMapping
    public Page<CommunicationLogDTO> searchCommunicationLogs(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return communicationLogService.searchAll(request);
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createCommunicationLog( @PathVariable UUID locationId,
            @RequestBody @Valid final CommunicationLogDTO communicationLogDTO) {

        communicationLogDTO.setLocation(locationId);

        final UUID createdId = communicationLogService.create(communicationLogDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateCommunicationLog(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final CommunicationLogDTO communicationLogDTO) {

        communicationLogDTO.setLocation(locationId);

        communicationLogService.update(id, communicationLogDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCommunicationLog(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        communicationLogService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
