package co.tz.settlo.api.payslip;

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
@RequestMapping(value = "/api/payslips", produces = MediaType.APPLICATION_JSON_VALUE)
public class PayslipResource {

    private final PayslipService payslipService;

    public PayslipResource(final PayslipService payslipService) {
        this.payslipService = payslipService;
    }

    @GetMapping
    public ResponseEntity<List<PayslipDTO>> getAllPayslips() {
        return ResponseEntity.ok(payslipService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayslipDTO> getPayslip(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(payslipService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createPayslip(@RequestBody @Valid final PayslipDTO payslipDTO) {
        final UUID createdId = payslipService.create(payslipDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updatePayslip(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final PayslipDTO payslipDTO) {
        payslipService.update(id, payslipDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePayslip(@PathVariable(name = "id") final UUID id) {
        payslipService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
