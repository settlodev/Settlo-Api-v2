package co.tz.settlo.api.sender_id;

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
@RequestMapping(value = "/api/sender-id/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Sender ID Endpoints")
public class SenderIdResource {

    private final SenderIdService senderIdService;

    public SenderIdResource(final SenderIdService senderIdService) {
        this.senderIdService = senderIdService;
    }

    @GetMapping
    @Operation(summary = "Get all Sender IDs")
    public ResponseEntity<List<SenderIdDTO>> getAllSenderIds() {
        return ResponseEntity.ok(senderIdService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Sender ID")
    public ResponseEntity<SenderIdDTO> getSenderId(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(senderIdService.get(id));
    }

    @PostMapping("/create")
    @Operation(summary = "Create a Sender ID")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createSenderId(@RequestBody @Valid final SenderIdDTO senderIdDTO) {
        final UUID createdId = senderIdService.create(senderIdDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Sender ID")
    public ResponseEntity<UUID> updateSenderId(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final SenderIdDTO senderIdDTO) {
        senderIdService.update(id, senderIdDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a Sender ID")
    public ResponseEntity<Void> deleteSenderId(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = senderIdService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        senderIdService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
