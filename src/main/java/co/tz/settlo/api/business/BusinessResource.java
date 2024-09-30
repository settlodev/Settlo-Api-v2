package co.tz.settlo.api.business;

import co.tz.settlo.api.location.LocationDTO;
import co.tz.settlo.api.util.ReferencedException;
import co.tz.settlo.api.util.ReferencedWarning;
import co.tz.settlo.api.util.RestApiFilter.FieldType;
import co.tz.settlo.api.util.RestApiFilter.FilterRequest;
import co.tz.settlo.api.util.RestApiFilter.Operator;
import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping(value = "/api/businesses/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Business Endpoints")
public class BusinessResource {

    private final BusinessService businessService;

    public BusinessResource(final BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping
    @Operation(summary = "Get All Businesses", description = "Get all businesses under a certain user")
    public ResponseEntity<List<BusinessResponseDTO>> getAllBusinesses(@PathVariable final UUID userId) {
        return ResponseEntity.ok(businessService.findAll(userId));
    }


    @PostMapping
    @Operation(summary = "Search Businesses")
    public Page<BusinessResponseDTO> searchBusiness(@PathVariable final UUID userId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest businessFilter = new FilterRequest();
        businessFilter.setKey("user.id");
        businessFilter.setOperator(Operator.EQUAL);
        businessFilter.setFieldType(FieldType.UUID_STRING);
        businessFilter.setValue(userId);

        request.getFilters().add(businessFilter);

        return businessService.searchAll(request);
    }



    @GetMapping("/{id}")
    @Operation(summary = "Get a Business", description = "Get a business by supplying it's ID")
    public ResponseEntity<BusinessResponseDTO> getBusiness(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(businessService.get(id));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a business", description = "Create a business by supplying it's body as JSON")
    public ResponseEntity<UUID> createBusiness(@RequestBody @Valid final BusinessCreateDTO businessDTO) {
        final UUID createdId = businessService.create(businessDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a business", description = "Update a business by supplying it's new body as JSON")
    public ResponseEntity<UUID> updateBusiness(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final BusinessDTO businessDTO) {
        businessService.update(id, businessDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a Business", description = "Delete a business by supplying it's ID")
    public ResponseEntity<Void> deleteBusiness(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = businessService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        businessService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
