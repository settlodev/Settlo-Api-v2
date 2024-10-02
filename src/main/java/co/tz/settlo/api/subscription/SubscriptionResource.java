package co.tz.settlo.api.subscription;

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
@RequestMapping(value = "/api/subscriptions/", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Subscription Endpoints")
public class SubscriptionResource {

    private final SubscriptionService subscriptionService;

    public SubscriptionResource(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    @Operation(summary = "Get all Subscriptions")
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Subscription")
    public ResponseEntity<SubscriptionDTO> getSubscription(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(subscriptionService.get(id));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a Subscription")
    public ResponseEntity<UUID> createSubscription(
            @RequestBody @Valid final SubscriptionDTO subscriptionDTO) {
        final UUID createdId = subscriptionService.create(subscriptionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Subscription")
    public ResponseEntity<UUID> updateSubscription(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final SubscriptionDTO subscriptionDTO) {
        subscriptionService.update(id, subscriptionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a Subscription")
    public ResponseEntity<Void> deleteSubscription(@PathVariable(name = "id") final UUID id) {
        subscriptionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
