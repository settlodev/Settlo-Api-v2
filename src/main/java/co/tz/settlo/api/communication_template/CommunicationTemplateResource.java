package co.tz.settlo.api.communication_template;

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
@RequestMapping(value = "/api/communicationTemplates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommunicationTemplateResource {

    private final CommunicationTemplateService communicationTemplateService;

    public CommunicationTemplateResource(
            final CommunicationTemplateService communicationTemplateService) {
        this.communicationTemplateService = communicationTemplateService;
    }

    @GetMapping
    public ResponseEntity<List<CommunicationTemplateDTO>> getAllCommunicationTemplates() {
        return ResponseEntity.ok(communicationTemplateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunicationTemplateDTO> getCommunicationTemplate(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(communicationTemplateService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createCommunicationTemplate(
            @RequestBody @Valid final CommunicationTemplateDTO communicationTemplateDTO) {
        final UUID createdId = communicationTemplateService.create(communicationTemplateDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateCommunicationTemplate(
            @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final CommunicationTemplateDTO communicationTemplateDTO) {
        communicationTemplateService.update(id, communicationTemplateDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCommunicationTemplate(
            @PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = communicationTemplateService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        communicationTemplateService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
