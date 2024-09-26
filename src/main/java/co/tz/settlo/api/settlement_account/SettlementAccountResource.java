package co.tz.settlo.api.settlement_account;

import co.tz.settlo.api.util.ReferencedException;
import co.tz.settlo.api.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Settlement Accounts Endpoints")
public class SettlementAccountResource {

    private final SettlementAccountService settlementAccountService;

    public SettlementAccountResource(final SettlementAccountService settlementAccountService) {
        this.settlementAccountService = settlementAccountService;
    }

    @GetMapping
    @Operation(summary = "Get all settlement accounts")
    public ResponseEntity<List<SettlementAccountDTO>> getAllSettlementAccounts() {
        return ResponseEntity.ok(settlementAccountService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a settlement account")
    public ResponseEntity<SettlementAccountDTO> getSettlementAccount(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(settlementAccountService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a settlement account")
    public ResponseEntity<UUID> createSettlementAccount(
            @RequestBody @Valid final SettlementAccountDTO settlementAccountDTO) {
        final UUID createdId = settlementAccountService.create(settlementAccountDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a settlement account")
    public ResponseEntity<UUID> updateSettlementAccount(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final SettlementAccountDTO settlementAccountDTO) {
        settlementAccountService.update(id, settlementAccountDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a settlement account")
    public ResponseEntity<Void> deleteSettlementAccount(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = settlementAccountService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        settlementAccountService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
