package co.tz.settlo.api.controllers.tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TagDTO {

    private UUID id;

    @NotNull
    @Size(max = 100)
    @TagNameUnique
    private String name;

    private Boolean status;

    @JsonProperty("isArchived")
    private Boolean isArchived;

    private Boolean canDelete;

    private UUID locationId;
}
