package co.tz.settlo.api.tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class TagResponseDTO {

    private UUID id;

    @NotNull
    @Size(max = 100)
    @TagNameUnique
    private String name;

    @NotNull
    private Boolean status;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private Boolean canDelete;

    private UUID locationId;
}
