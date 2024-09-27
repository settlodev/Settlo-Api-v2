package co.tz.settlo.api.reservation;

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
@RequestMapping(value = "/api/reservations/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reservations Endpoints")
public class ReservationResource {

    private final ReservationService reservationService;

    public ReservationResource(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @Operation(summary = "Get all Reservations")
    public ResponseEntity<List<ReservationDTO>> getAllReservations(@PathVariable UUID locationId) {
        return ResponseEntity.ok(reservationService.findAll(locationId));
    }

    @PostMapping
    @Operation(summary = "Search Reservations")
    public Page<ReservationDTO> searchReservations(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location.id");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.UUID_STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return reservationService.searchAll(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a reservation")
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(reservationService.get(id));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a reservation")
    public ResponseEntity<UUID> createReservation(@PathVariable UUID locationId,
                                                  @RequestBody @Valid final ReservationDTO reservationDTO) {

        reservationDTO.setLocation(locationId);

        final UUID createdId = reservationService.create(reservationDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a reservation")
    public ResponseEntity<UUID> updateReservation(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final ReservationDTO reservationDTO) {

        reservationDTO.setLocation(locationId);

        reservationService.update(id, reservationDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a reservation")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
