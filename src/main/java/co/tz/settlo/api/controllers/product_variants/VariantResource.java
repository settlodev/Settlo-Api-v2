package co.tz.settlo.api.controllers.product_variants;

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
@RequestMapping(value = "/api/variants/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Variant Endpoints")
public class VariantResource {

    private final VariantService variantService;

    public VariantResource(final VariantService variantService) {
        this.variantService = variantService;
    }

    @GetMapping
    @Operation(summary = "Get all variants")
    public ResponseEntity<List<VariantDTO>> getAllVariants(@PathVariable UUID productId) {
        return ResponseEntity.ok(variantService.findAll(productId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a variant")
    public ResponseEntity<VariantDTO> getVariant(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(variantService.get(id));
    }

    @PostMapping
    @Operation(summary = "Search variants")
    public Page<VariantDTO> searchProductVariants(@PathVariable UUID productId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("product");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.STRING);
        locationFilter.setValue(productId);

        request.getFilters().add(locationFilter);

        return variantService.searchAll(request);
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a variant")
    public ResponseEntity<UUID> createVariant(@RequestBody @Valid final VariantDTO variantDTO) {
        final UUID createdId = variantService.create(variantDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a variant")
    public ResponseEntity<UUID> updateVariant(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final VariantDTO variantDTO) {
        variantService.update(id, variantDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a variant")
    public ResponseEntity<Void> deleteVariant(@PathVariable(name = "id") final UUID id) {
        variantService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
