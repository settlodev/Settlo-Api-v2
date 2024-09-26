package co.tz.settlo.api.product_variants;

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
@RequestMapping(value = "/api/variants", produces = MediaType.APPLICATION_JSON_VALUE)
public class VariantResource {

    private final VariantService variantService;

    public VariantResource(final VariantService variantService) {
        this.variantService = variantService;
    }

    @GetMapping
    public ResponseEntity<List<VariantDTO>> getAllVariants() {
        return ResponseEntity.ok(variantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VariantDTO> getVariant(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(variantService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createVariant(@RequestBody @Valid final VariantDTO variantDTO) {
        final UUID createdId = variantService.create(variantDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateVariant(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final VariantDTO variantDTO) {
        variantService.update(id, variantDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteVariant(@PathVariable(name = "id") final UUID id) {
        variantService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
