package co.tz.settlo.api.stock_variant;

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
@RequestMapping(value = "/api/stockVariants", produces = MediaType.APPLICATION_JSON_VALUE)
public class StockVariantResource {

    private final StockVariantService stockVariantService;

    public StockVariantResource(final StockVariantService stockVariantService) {
        this.stockVariantService = stockVariantService;
    }

    @GetMapping
    public ResponseEntity<List<StockVariantDTO>> getAllStockVariants() {
        return ResponseEntity.ok(stockVariantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockVariantDTO> getStockVariant(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(stockVariantService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createStockVariant(
            @RequestBody @Valid final StockVariantDTO stockVariantDTO) {
        final UUID createdId = stockVariantService.create(stockVariantDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateStockVariant(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final StockVariantDTO stockVariantDTO) {
        stockVariantService.update(id, stockVariantDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteStockVariant(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = stockVariantService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        stockVariantService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
