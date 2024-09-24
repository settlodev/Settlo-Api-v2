package co.tz.settlo.api.location_setting;

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
@RequestMapping(value = "/api/locationSettings", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocationSettingResource {

    private final LocationSettingService locationSettingService;

    public LocationSettingResource(final LocationSettingService locationSettingService) {
        this.locationSettingService = locationSettingService;
    }

    @GetMapping
    public ResponseEntity<List<LocationSettingDTO>> getAllLocationSettings() {
        return ResponseEntity.ok(locationSettingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationSettingDTO> getLocationSetting(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(locationSettingService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createLocationSetting(
            @RequestBody @Valid final LocationSettingDTO locationSettingDTO) {
        final UUID createdId = locationSettingService.create(locationSettingDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateLocationSetting(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final LocationSettingDTO locationSettingDTO) {
        locationSettingService.update(id, locationSettingDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteLocationSetting(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = locationSettingService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        locationSettingService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
