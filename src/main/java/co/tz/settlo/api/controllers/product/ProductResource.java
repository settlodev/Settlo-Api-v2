package co.tz.settlo.api.controllers.product;

import co.tz.settlo.api.csv_parsers.product_csv_parser.ProductsCsv;
import co.tz.settlo.api.util.ReferencedException;
import co.tz.settlo.api.util.ReferencedWarning;
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
@RequestMapping(value = "/api/products/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Products Endpoints")
public class ProductResource {

    private final ProductService productService;

    public ProductResource(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(@PathVariable UUID locationId) {
        return ResponseEntity.ok(productService.findAll(locationId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(productService.get(id));
    }

    @PostMapping
    @Operation(summary = "Search a product")
    public Page<ProductResponseDTO> searchProducts(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location.id");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.UUID_STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return productService.searchAll(request);
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a product")
    public ResponseEntity<UUID> createProduct(@PathVariable UUID locationId, @RequestBody @Valid final ProductDTO productDTO) {

        productDTO.setLocation(locationId);

        final UUID createdId = productService.create(productDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product")
    public ResponseEntity<UUID> updateProduct(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final ProductDTO productDTO) {

        productDTO.setLocation(locationId);

        productService.update(id, productDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a product")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = productService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
