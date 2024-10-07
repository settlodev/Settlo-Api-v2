package co.tz.settlo.api.controllers.kyc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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
@RequestMapping(value = "/api/kyc/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Kyc Endpoints")
public class KycResource {

    private final KycService kycService;

    public KycResource(final KycService kycService) {
        this.kycService = kycService;
    }

    @GetMapping
    @Operation(summary = "Get all Kycs")
    public ResponseEntity<KycDTO> getUserKyc(@PathVariable UUID userId) {
        return ResponseEntity.ok(kycService.findByUserId(userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Kyc")
    public ResponseEntity<KycDTO> getKyc(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(kycService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create Kyc")
    public ResponseEntity<UUID> createKyc(@PathVariable UUID userId, @RequestBody @Valid final KycDTO kycDTO) {
        kycDTO.setUser(userId);

        final UUID createdId = kycService.create(kycDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Kyc")
    public ResponseEntity<UUID> updateKyc(@PathVariable UUID userId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final KycDTO kycDTO) {

        kycDTO.setUser(userId);

        kycService.update(id, kycDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete Kyc")
    public ResponseEntity<Void> deleteKyc(@PathVariable UUID userId, @PathVariable(name = "id") final UUID id) {
        kycService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
