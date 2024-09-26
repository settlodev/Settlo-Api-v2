package co.tz.settlo.api.location;

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
@RequestMapping(value = "/api/locations/{businessId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Location Endpoints")
public class LocationResource {

    private final LocationService locationService;

    public LocationResource(final LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    @Operation(summary = "Search Locations")
    public Page<LocationDTO> searchLocations(@PathVariable UUID businessId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest businessFilter = new FilterRequest();
        businessFilter.setKey("business");
        businessFilter.setOperator(Operator.EQUAL);
        businessFilter.setFieldType(FieldType.STRING);
        businessFilter.setValue(businessId);

        request.getFilters().add(businessFilter);

        return locationService.searchAll(request);
    }

    @GetMapping
    @Operation(summary = "Get all Locations")
    public ResponseEntity<List<LocationDTO>> getAllLocations(@PathVariable final UUID businessId) {
        return ResponseEntity.ok(locationService.findAll(businessId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Location")
    public ResponseEntity<LocationDTO> getLocation(@PathVariable final UUID businessId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(locationService.get(id));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "create a Location")
    public ResponseEntity<UUID> createLocation(@PathVariable final UUID businessId, @RequestBody @Valid final LocationDTO locationDTO) {
        locationDTO.setBusiness(businessId);

        final UUID createdId = locationService.create(locationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Location")
    public ResponseEntity<UUID> updateLocation(@PathVariable final UUID businessId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final LocationDTO locationDTO) {
        locationDTO.setBusiness(businessId);

        locationService.update(id, locationDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a Location")
    public ResponseEntity<Void> deleteLocation(@PathVariable final UUID businessId, @PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = locationService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
