package co.tz.settlo.api.stock_usage;

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
@RequestMapping(value = "/api/stockUsages", produces = MediaType.APPLICATION_JSON_VALUE)
public class StockUsageResource {

    private final StockUsageService stockUsageService;

    public StockUsageResource(final StockUsageService stockUsageService) {
        this.stockUsageService = stockUsageService;
    }

    @GetMapping
    public ResponseEntity<List<StockUsageDTO>> getAllStockUsages() {
        return ResponseEntity.ok(stockUsageService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockUsageDTO> getStockUsage(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(stockUsageService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createStockUsage(
            @RequestBody @Valid final StockUsageDTO stockUsageDTO) {
        final UUID createdId = stockUsageService.create(stockUsageDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateStockUsage(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final StockUsageDTO stockUsageDTO) {
        stockUsageService.update(id, stockUsageDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteStockUsage(@PathVariable(name = "id") final UUID id) {
        stockUsageService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
