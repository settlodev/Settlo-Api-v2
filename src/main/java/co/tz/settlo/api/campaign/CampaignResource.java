package co.tz.settlo.api.campaign;

import co.tz.settlo.api.util.ReferencedException;
import co.tz.settlo.api.util.ReferencedWarning;
import co.tz.settlo.api.util.RestApiFilter.FieldType;
import co.tz.settlo.api.util.RestApiFilter.FilterRequest;
import co.tz.settlo.api.util.RestApiFilter.Operator;
import co.tz.settlo.api.util.RestApiFilter.SearchRequest;
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
@RequestMapping(value = "/api/campaigns/{locationId}", produces = MediaType.APPLICATION_JSON_VALUE)
public class CampaignResource {

    private final CampaignService campaignService;

    public CampaignResource(final CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public ResponseEntity<List<CampaignDTO>> getAllCampaigns(@PathVariable final UUID locationId) {
        return ResponseEntity.ok(campaignService.findAll(locationId));
    }

    @PostMapping
    public Page<CampaignDTO> searchCampaign(@PathVariable UUID locationId, @RequestBody SearchRequest request) {
        // Enforce Location filter
        FilterRequest locationFilter = new FilterRequest();
        locationFilter.setKey("location");
        locationFilter.setOperator(Operator.EQUAL);
        locationFilter.setFieldType(FieldType.STRING);
        locationFilter.setValue(locationId);

        request.getFilters().add(locationFilter);

        return campaignService.searchAll(request);
    }

    @GetMapping("/{id}")
    @Tag(name= "Get Campaign", description = "Get a Campaign")
    public ResponseEntity<CampaignDTO> getCampaign(@PathVariable final UUID locationId, @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(campaignService.get(id));
    }

    @PostMapping("/create")
    @ApiResponse(responseCode = "201")
    @Tag(name = "Create a Campaign", description = "Create a campaign by submitting it's body")
    public ResponseEntity<UUID> createCampaign(@PathVariable final UUID locationId, @RequestBody @Valid final CampaignDTO campaignDTO) {
        campaignDTO.setLocation(locationId);

        final UUID createdId = campaignService.create(campaignDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Tag(name = "Update a Campaign", description = "Update a campaign")
    public ResponseEntity<UUID> updateCampaign(@PathVariable final UUID locationId, @PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final CampaignDTO campaignDTO) {
        campaignDTO.setLocation(locationId);

        campaignService.update(id, campaignDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @Tag(name = "Delete a Campaign")
    public ResponseEntity<Void> deleteCampaign(@PathVariable final UUID locationId, @PathVariable(name = "id") final UUID id) {
        final ReferencedWarning referencedWarning = campaignService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        campaignService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
