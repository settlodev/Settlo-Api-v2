package co.tz.settlo.api.controllers.payslip;

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
@RequestMapping(value = "/api/payslips/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payslips Endpoints")
public class PayslipResource {

    private final PayslipService payslipService;

    public PayslipResource(final PayslipService payslipService) {
        this.payslipService = payslipService;
    }

    @GetMapping
    @Operation(summary = "Get all payslips")
    public ResponseEntity<List<PayslipDTO>> getAllPayslips(@PathVariable UUID locationId) {
        return ResponseEntity.ok(payslipService.findAll(locationId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a payslip")
    public ResponseEntity<PayslipDTO> getPayslip(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(payslipService.get(id));
    }

    @PostMapping
    @Operation(summary = "Search payslips")
    public Page<PayslipDTO> searchPayslips(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location.id");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.UUID_STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return payslipService.searchAll(request);
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a payslip")
    public ResponseEntity<UUID> createPayslip(@PathVariable UUID locationId, @RequestBody @Valid final PayslipDTO payslipDTO) {

        payslipDTO.setLocation(locationId);

        final UUID createdId = payslipService.create(payslipDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a payslip")
    public ResponseEntity<UUID> updatePayslip(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final PayslipDTO payslipDTO) {

        payslipDTO.setLocation(locationId);

        payslipService.update(id, payslipDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a payslip")
    public ResponseEntity<Void> deletePayslip(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        payslipService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
