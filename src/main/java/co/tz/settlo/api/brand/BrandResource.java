package co.tz.settlo.api.brand;

import co.tz.settlo.api.addon.AddonDTO;
import co.tz.settlo.api.util.ReferencedException;
import co.tz.settlo.api.util.ReferencedWarning;
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
@RequestMapping(value = "/api/brands/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Brand Endpoints")
public class BrandResource {

    private final BrandService brandService;

    public BrandResource(final BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    @Operation(summary = "Get all Brands", description = "Get all brands under a location id")
    public ResponseEntity<List<BrandDTO>> getAllBrands(@PathVariable UUID locationId) {
        return ResponseEntity.ok(brandService.findAll(locationId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Brand", description = "Get a specific Brand by providing it's ID")
    public ResponseEntity<BrandDTO> getBrand(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(brandService.get(id));
    }

    @PostMapping
    @Operation(summary = "Search Brands", description = "Search a brand")
    public Page<BrandDTO> searchBrands(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location.id");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.UUID_STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return brandService.searchAll(request);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a Brand", description = "Create a Brand by supplying it's JSON as HTTP body")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createBrand(@PathVariable UUID locationId, @RequestBody @Valid final BrandDTO brandDTO) {
        brandDTO.setLocation(locationId);

        final UUID createdId = brandService.create(brandDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Brand", description = "Update a Brand by suppying it's new JSON as HTTP body")
    public ResponseEntity<UUID> updateBrand(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final BrandDTO brandDTO) {
        brandDTO.setLocation(locationId);

        brandService.update(id, brandDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete Brand", description = "Delete a brand by specifying it's ID")
    public ResponseEntity<Void> deleteBrand(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = brandService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        brandService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
