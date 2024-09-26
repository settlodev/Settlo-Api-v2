package co.tz.settlo.api.stock_usage;

import co.tz.settlo.api.util.RestApiFilter.FieldType;
import co.tz.settlo.api.util.RestApiFilter.FilterRequest;
import co.tz.settlo.api.util.RestApiFilter.Operator;
import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping(value = "/api/stock-usage/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
public class StockUsageResource {

    private final StockUsageService stockUsageService;

    public StockUsageResource(final StockUsageService stockUsageService) {
        this.stockUsageService = stockUsageService;
    }

    @GetMapping
    public ResponseEntity<List<StockUsageDTO>> getAllStockUsages(@PathVariable UUID locationId) {
        return ResponseEntity.ok(stockUsageService.findAll(locationId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockUsageDTO> getStockUsage(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(stockUsageService.get(id));
    }

    @PostMapping
    public Page<StockUsageDTO> searchStockUsage(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return stockUsageService.searchAll(request);
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createStockUsage( @PathVariable UUID locationId,
            @RequestBody @Valid final StockUsageDTO stockUsageDTO) {

        stockUsageDTO.setLocation(locationId);

        final UUID createdId = stockUsageService.create(stockUsageDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateStockUsage(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final StockUsageDTO stockUsageDTO) {

        stockUsageDTO.setLocation(locationId);

        stockUsageService.update(id, stockUsageDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteStockUsage(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        stockUsageService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
