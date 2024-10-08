package co.tz.settlo.api.controllers.role;

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
@RequestMapping(value = "/api/roles/{businessId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Roles Endpoints")
public class RoleResource {

    private final RoleService roleService;

    public RoleResource(final RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @Operation(summary = "Get all Roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles(@PathVariable final UUID businessId) {
        return ResponseEntity.ok(roleService.findAll(businessId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Role")
    public ResponseEntity<RoleDTO> getRole(@PathVariable final UUID businessId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(roleService.get(id));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a Role")
    public ResponseEntity<UUID> createRole(@PathVariable final UUID businessId, @RequestBody @Valid final RoleDTO roleDTO) {
        final UUID createdId = roleService.create(roleDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Role")
    public ResponseEntity<UUID> updateRole(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final RoleDTO roleDTO) {
        roleService.update(id, roleDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a Role")
    public ResponseEntity<Void> deleteRole(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = roleService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
