package co.tz.settlo.api.controllers.brand;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BrandDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    @BrandNameUnique
    private String name;

    private Boolean status = true;

    private Boolean canDelete = true;

    @JsonProperty("isArchived")
    private Boolean isArchived = false;

    private UUID location;
}
