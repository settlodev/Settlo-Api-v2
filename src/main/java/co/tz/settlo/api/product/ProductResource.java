package co.tz.settlo.api.product;

import co.tz.settlo.api.util.ReferencedException;
import co.tz.settlo.api.util.ReferencedWarning;
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
@RequestMapping(value = "/api/products/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductResource {

    private final ProductService productService;

    public ProductResource(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(@PathVariable UUID locationId) {
        return ResponseEntity.ok(productService.findAll(locationId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(productService.get(id));
    }

    @PostMapping
    public Page<ProductDTO> searchProducts(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return productService.searchAll(request);
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createProduct(@PathVariable UUID locationId, @RequestBody @Valid final ProductDTO productDTO) {

        productDTO.setLocation(locationId);

        final UUID createdId = productService.create(productDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateProduct(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final ProductDTO productDTO) {

        productDTO.setLocation(locationId);

        productService.update(id, productDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = productService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
