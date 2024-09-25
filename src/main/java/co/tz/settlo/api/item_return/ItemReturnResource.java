package co.tz.settlo.api.item_return;

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
@RequestMapping(value = "/api/returns/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemReturnResource {

    private final ItemReturnService itemReturnService;

    public ItemReturnResource(final ItemReturnService itemReturnService) {
        this.itemReturnService = itemReturnService;
    }

    @GetMapping
    public ResponseEntity<List<ItemReturnDTO>> getAllItemReturns(@PathVariable UUID locationId) {
        return ResponseEntity.ok(itemReturnService.findAll(locationId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemReturnDTO> getItemReturn(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(itemReturnService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createItemReturn( @PathVariable UUID locationId,
            @RequestBody @Valid final ItemReturnDTO itemReturnDTO) {

        itemReturnDTO.setLocation(locationId);

        final UUID createdId = itemReturnService.create(itemReturnDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateItemReturn(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final ItemReturnDTO itemReturnDTO) {
        itemReturnDTO.setLocation(locationId);

        itemReturnService.update(id, itemReturnDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteItemReturn(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        itemReturnService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
