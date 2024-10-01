package co.tz.settlo.api.location_setting;

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
@RequestMapping(value = "/api/location-settings/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Location Setting Endpoints")
public class LocationSettingResource {

    private final LocationSettingService locationSettingService;

    public LocationSettingResource(final LocationSettingService locationSettingService) {
        this.locationSettingService = locationSettingService;
    }

    @GetMapping
    @Operation(summary = "Get all location settings")
    public ResponseEntity<LocationSettingDTO> getLocationSettingsByLocation(@PathVariable UUID locationId) {
        return ResponseEntity.ok(locationSettingService.findByLocationId(locationId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a location settings")
    public ResponseEntity<LocationSettingDTO> getLocationSetting(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(locationSettingService.get(id));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a location setting")
    public ResponseEntity<UUID> createLocationSetting(@PathVariable UUID locationId, @RequestBody @Valid final LocationSettingCreateDTO locationSettingDTO) {

        locationSettingDTO.setLocationId(locationId);

        final UUID createdId = locationSettingService.create(locationSettingDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a location setting")
    public ResponseEntity<UUID> updateLocationSetting(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final LocationSettingDTO locationSettingDTO) {

        locationSettingDTO.setLocationId(locationId);

        locationSettingService.update(id, locationSettingDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a location setting")
    public ResponseEntity<Void> deleteLocationSetting(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = locationSettingService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        locationSettingService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
