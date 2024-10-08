package co.tz.settlo.api.controllers.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoleDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

    @NotNull
    private Boolean canDelete = true;

    @NotNull
    private Boolean status = true;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived  = false;

    @NotNull
    private UUID business;

}
