package co.tz.settlo.api.kyc;

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
@RequestMapping(value = "/api/kycs", produces = MediaType.APPLICATION_JSON_VALUE)
public class KycResource {

    private final KycService kycService;

    public KycResource(final KycService kycService) {
        this.kycService = kycService;
    }

    @GetMapping
    public ResponseEntity<List<KycDTO>> getAllKycs() {
        return ResponseEntity.ok(kycService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KycDTO> getKyc(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(kycService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createKyc(@RequestBody @Valid final KycDTO kycDTO) {
        final UUID createdId = kycService.create(kycDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateKyc(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final KycDTO kycDTO) {
        kycService.update(id, kycDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteKyc(@PathVariable(name = "id") final UUID id) {
        kycService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
