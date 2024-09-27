package co.tz.settlo.api.tag;

import co.tz.settlo.api.util.ReferencedException;
import co.tz.settlo.api.util.ReferencedWarning;
import co.tz.settlo.api.util.RestApiFilter.FieldType;
import co.tz.settlo.api.util.RestApiFilter.FilterRequest;
import co.tz.settlo.api.util.RestApiFilter.Operator;
import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping(value = "/api/tags/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Tag Endpoints")
public class TagResource {

    private final TagService tagService;

    public TagResource(final TagService tagService) {
        this.tagService = tagService;
    }

    
    @PostMapping
    @Operation(summary = "Search Tags")
    public Page<TagDTO> searchTags(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location.id");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.UUID_STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return tagService.searchAll(request);
    }

    @GetMapping
    @Operation(summary = "Get all Tags")
    public ResponseEntity<List<TagDTO>> getAllTags(@PathVariable UUID locationId) {
        return ResponseEntity.ok(tagService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Tag")
    public ResponseEntity<TagDTO> getTag(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(tagService.get(id));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a Tag")
    public ResponseEntity<UUID> createTag(@PathVariable(name = "locationId") final UUID locationId, @RequestBody @Valid final TagDTO tagDTO) {
        tagDTO.setLocationId(locationId);

        final UUID createdId = tagService.create(tagDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Tag")
    public ResponseEntity<UUID> updateTag(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final TagDTO tagDTO) {

        tagDTO.setLocationId(locationId);

        tagService.update(id, tagDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a Tag")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID locationId, @PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = tagService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
