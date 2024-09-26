package co.tz.settlo.api.settlement;

import co.tz.settlo.api.reservation.ReservationDTO;
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
@RequestMapping(value = "/api/settlements/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
public class SettlementResource {

    private final SettlementService settlementService;

    public SettlementResource(final SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @GetMapping
    public ResponseEntity<List<SettlementDTO>> getAllSettlements(@PathVariable UUID locationId) {
        return ResponseEntity.ok(settlementService.findAll(locationId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SettlementDTO> getSettlement(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(settlementService.get(id));
    }

    @PostMapping
    public Page<SettlementDTO> searchSettlements(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return settlementService.searchAll(request);
    }
    
    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createSettlement( @PathVariable UUID locationId,
            @RequestBody @Valid final SettlementDTO settlementDTO) {

        settlementDTO.setLocation(locationId);

        final UUID createdId = settlementService.create(settlementDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateSettlement(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final SettlementDTO settlementDTO) {

        settlementDTO.setLocation(locationId);

        settlementService.update(id, settlementDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSettlement(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        settlementService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
