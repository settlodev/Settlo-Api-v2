package co.tz.settlo.api.controllers.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryResponseDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    @CategoryNameUnique
    private String name;

    @Size(max = 255)
    private String image;

    @CategoryParentIdUnique
    private UUID parentId;

    @NotNull
    private Boolean status;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private Boolean canDelete;

    private UUID location;

    private String locationName;
}
