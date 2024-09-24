package co.tz.settlo.api.settlement_account;

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
@RequestMapping(value = "/api/settlementAccounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class SettlementAccountResource {

    private final SettlementAccountService settlementAccountService;

    public SettlementAccountResource(final SettlementAccountService settlementAccountService) {
        this.settlementAccountService = settlementAccountService;
    }

    @GetMapping
    public ResponseEntity<List<SettlementAccountDTO>> getAllSettlementAccounts() {
        return ResponseEntity.ok(settlementAccountService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SettlementAccountDTO> getSettlementAccount(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(settlementAccountService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createSettlementAccount(
            @RequestBody @Valid final SettlementAccountDTO settlementAccountDTO) {
        final UUID createdId = settlementAccountService.create(settlementAccountDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateSettlementAccount(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final SettlementAccountDTO settlementAccountDTO) {
        settlementAccountService.update(id, settlementAccountDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSettlementAccount(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = settlementAccountService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        settlementAccountService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
