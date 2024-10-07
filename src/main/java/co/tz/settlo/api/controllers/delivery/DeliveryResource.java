package co.tz.settlo.api.controllers.delivery;

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
@RequestMapping(value = "/api/deliveries/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Delivery Endpoints")
public class DeliveryResource {

    private final DeliveryService deliveryService;

    public DeliveryResource(final DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    @Operation(summary = "Get all Deliveries")
    public ResponseEntity<List<DeliveryDTO>> getAllDeliveries(@PathVariable UUID locationId) {
        return ResponseEntity.ok(deliveryService.findAll(locationId));
    }

    @PostMapping
    @Operation(summary = "Search all Deliveries")
    public Page<DeliveryDTO> searchDeliveries(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location.id");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.UUID_STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return deliveryService.searchAll(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Deliveries")
    public ResponseEntity<DeliveryDTO> getDelivery(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(deliveryService.get(id));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a Deliveries")
    public ResponseEntity<UUID> createDelivery(@PathVariable UUID locationId, @RequestBody @Valid final DeliveryDTO deliveryDTO) {

        deliveryDTO.setLocation(locationId);

        final UUID createdId = deliveryService.create(deliveryDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Deliveries")
    public ResponseEntity<UUID> updateDelivery(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final DeliveryDTO deliveryDTO) {

        deliveryDTO.setLocation(locationId);

        deliveryService.update(id, deliveryDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a Deliveries")
    public ResponseEntity<Void> deleteDelivery(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
