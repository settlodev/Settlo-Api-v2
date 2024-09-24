package co.tz.settlo.api.refund;

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
@RequestMapping(value = "/api/refunds", produces = MediaType.APPLICATION_JSON_VALUE)
public class RefundResource {

    private final RefundService refundService;

    public RefundResource(final RefundService refundService) {
        this.refundService = refundService;
    }

    @GetMapping
    public ResponseEntity<List<RefundDTO>> getAllRefunds() {
        return ResponseEntity.ok(refundService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RefundDTO> getRefund(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(refundService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createRefund(@RequestBody @Valid final RefundDTO refundDTO) {
        final UUID createdId = refundService.create(refundDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateRefund(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final RefundDTO refundDTO) {
        refundService.update(id, refundDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRefund(@PathVariable(name = "id") final UUID id) {
        refundService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
