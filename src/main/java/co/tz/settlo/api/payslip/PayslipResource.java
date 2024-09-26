package co.tz.settlo.api.payslip;

import co.tz.settlo.api.product.ProductDTO;
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
@RequestMapping(value = "/api/payslips/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
public class PayslipResource {

    private final PayslipService payslipService;

    public PayslipResource(final PayslipService payslipService) {
        this.payslipService = payslipService;
    }

    @GetMapping
    public ResponseEntity<List<PayslipDTO>> getAllPayslips(@PathVariable UUID locationId) {
        return ResponseEntity.ok(payslipService.findAll(locationId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayslipDTO> getPayslip(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(payslipService.get(id));
    }

    @PostMapping
    public Page<PayslipDTO> searchPayslips(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return payslipService.searchAll(request);
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createPayslip(@PathVariable UUID locationId, @RequestBody @Valid final PayslipDTO payslipDTO) {

        payslipDTO.setLocation(locationId);

        final UUID createdId = payslipService.create(payslipDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updatePayslip(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final PayslipDTO payslipDTO) {

        payslipDTO.setLocation(locationId);

        payslipService.update(id, payslipDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePayslip(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        payslipService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
