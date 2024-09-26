package co.tz.settlo.api.addon;

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

import co.tz.settlo.api.util.RestApiFilter.FieldType;
import co.tz.settlo.api.util.RestApiFilter.FilterRequest;
import co.tz.settlo.api.util.RestApiFilter.Operator;
import co.tz.settlo.api.util.RestApiFilter.SearchRequest;


@RestController
@RequestMapping(value = "/api/addons/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddonResource {

    private final AddonService addonService;

    public AddonResource(final AddonService addonService) {
        this.addonService = addonService;
    }
    
    @PostMapping
    public Page<AddonDTO> searchAddons(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return addonService.searchAll(request);
    }

    @GetMapping
    @Tag(name = "Get all Addons", description = "Get all addons under the supplied location ID")
    public ResponseEntity<List<AddonDTO>> getAllAddons(@PathVariable UUID locationId) {
        return ResponseEntity.ok(addonService.findAll(locationId));
    }

    @GetMapping("/{id}")
    @Tag(name = "Get Addon", description = "Get a specific addon by supplying it's ID and location ID")
    public ResponseEntity<AddonDTO> getAddon(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(addonService.get(id));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Tag(name = "Create Addon", description = "Create an Addon by providing it's JSON as a HTTP body")
    public ResponseEntity<UUID> createAddon(@PathVariable UUID locationId, @RequestBody @Valid final AddonDTO addonDTO) {
        addonDTO.setLocation(locationId);
        final UUID createdId = addonService.create(addonDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Tag(name = "Update Addon", description = "Update an Addon by providing the new one as a JSON HTTP body")
    public ResponseEntity<UUID> updateAddon(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final AddonDTO addonDTO) {
        addonDTO.setLocation(locationId);
        addonService.update(id, addonDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Tag(name = "Delete Addon", description = "Delete an Addon by providing it's ID:")
    public ResponseEntity<Void> deleteAddon(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        addonService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
