package co.tz.settlo.api.discount;

import co.tz.settlo.api.expense.ExpenseDTO;
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
@RequestMapping(value = "/api/discounts/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Discount Endpoints")
public class DiscountResource {

    private final DiscountService discountService;

    public DiscountResource(final DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    @Operation(summary = "Get all Discounts")
    public ResponseEntity<List<DiscountResponseDTO>> getAllDiscounts() {
        return ResponseEntity.ok(discountService.findAll());
    }

    @PostMapping
    @Operation(summary = "Search Discounts")
    public Page<DiscountResponseDTO> searchDiscounts(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location.id");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.UUID_STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return discountService.searchAll(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Discounts")
    public ResponseEntity<DiscountResponseDTO> getDiscount(@PathVariable final UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(discountService.get(id));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a Discount")
    public ResponseEntity<UUID> createDiscount(@PathVariable final UUID locationId, @RequestBody @Valid final DiscountDTO discountDTO) {
        discountDTO.setLocation(locationId);

        final UUID createdId = discountService.create(discountDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Discount")
    public ResponseEntity<UUID> updateDiscount(@PathVariable final UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final DiscountDTO discountDTO) {
        discountDTO.setLocation(locationId);

        discountService.update(id, discountDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a Discount")
    public ResponseEntity<Void> deleteDiscount(@PathVariable final UUID locationId, @PathVariable(name = "id") final UUID id) {
        discountService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
