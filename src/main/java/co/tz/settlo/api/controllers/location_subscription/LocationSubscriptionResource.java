package co.tz.settlo.api.controllers.location_subscription;

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
@RequestMapping(value = "/api/location_subscriptions/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Location Subscription Endpoints ")
public class LocationSubscriptionResource {

    private final LocationSubscriptionService locationSubscriptionService;

    public LocationSubscriptionResource(final LocationSubscriptionService subscriptionService) {
        this.locationSubscriptionService = subscriptionService;
    }

    @GetMapping
    @Operation(summary = "Get all Location Subscriptions")
    public ResponseEntity<List<LocationSubscriptionDTO>> getAllLocationSubscriptions() {
        return ResponseEntity.ok(locationSubscriptionService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Location Subscription")
    public ResponseEntity<LocationSubscriptionDTO> getLocationSubscription(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(locationSubscriptionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a Location Subscription")
    public ResponseEntity<UUID> createLocationSubscription(
            @RequestBody @Valid final LocationSubscriptionDTO subscriptionDTO) {
        final UUID createdId = locationSubscriptionService.create(subscriptionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Location Subscription")
    public ResponseEntity<UUID> updateLocationSubscription(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final LocationSubscriptionDTO subscriptionDTO) {
        locationSubscriptionService.update(id, subscriptionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a Location Subscription")
    public ResponseEntity<Void> deleteLocationSubscription(@PathVariable(name = "id") final UUID id) {
        locationSubscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
