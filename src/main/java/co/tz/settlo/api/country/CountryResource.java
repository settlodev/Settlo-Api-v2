package co.tz.settlo.api.country;

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
@RequestMapping(value = "/api/countries", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Country Endpoints")
public class CountryResource {

    private final CountryService countryService;

    public CountryResource(final CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @Operation(summary = "Get all Countries")
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        return ResponseEntity.ok(countryService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a country")
    public ResponseEntity<CountryDTO> getCountry(@PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(countryService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @Operation(summary = "Create a country")
    public ResponseEntity<UUID> createCountry(@RequestBody @Valid final CountryDTO countryDTO) {
        final UUID createdId = countryService.create(countryDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a country")
    public ResponseEntity<UUID> updateCountry(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final CountryDTO countryDTO) {
        countryService.update(id, countryDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Operation(summary = "Delete a country")
    public ResponseEntity<Void> deleteCountry(@PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = countryService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        countryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
