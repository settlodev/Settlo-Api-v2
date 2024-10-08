package co.tz.settlo.api.discount;

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
@RequestMapping(value = "/api/discounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class DiscountResource {

    private final DiscountService discountService;

    public DiscountResource(final DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    public ResponseEntity<List<DiscountDTO>> getAllDiscounts() {
        return ResponseEntity.ok(discountService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDTO> getDiscount(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(discountService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createDiscount(@RequestBody @Valid final DiscountDTO discountDTO) {
        final UUID createdId = discountService.create(discountDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateDiscount(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final DiscountDTO discountDTO) {
        discountService.update(id, discountDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDiscount(@PathVariable(name = "id") final UUID id) {
        discountService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
