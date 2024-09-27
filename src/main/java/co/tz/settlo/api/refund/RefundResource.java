package co.tz.settlo.api.refund;

import co.tz.settlo.api.product.ProductDTO;
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
@RequestMapping(value = "/api/refunds/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Refunds Endpoints")
public class RefundResource {

    private final RefundService refundService;

    public RefundResource(final RefundService refundService) {
        this.refundService = refundService;
    }

    @GetMapping
    @Operation(summary = "Get all Refunds")
    public ResponseEntity<List<RefundDTO>> getAllRefunds(@PathVariable UUID locationId) {
        return ResponseEntity.ok(refundService.findAll(locationId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a refund")
    public ResponseEntity<RefundDTO> getRefund(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(refundService.get(id));
    }

    @PostMapping
    @Operation(summary = "Search a refund")
    public Page<RefundDTO> searchRefunds(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location.id");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.UUID_STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return refundService.searchAll(request);
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a refund")
    public ResponseEntity<UUID> createRefund(@PathVariable UUID locationId, @RequestBody @Valid final RefundDTO refundDTO) {

        refundDTO.setLocationId(locationId);

        final UUID createdId = refundService.create(refundDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a refund")
    public ResponseEntity<UUID> updateRefund(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final RefundDTO refundDTO) {

        refundDTO.setLocationId(locationId);

        refundService.update(id, refundDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a refund")
    public ResponseEntity<Void> deleteRefund(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        refundService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
