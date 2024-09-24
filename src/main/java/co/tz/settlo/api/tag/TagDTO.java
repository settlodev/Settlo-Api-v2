package co.tz.settlo.api.tag;

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

    @NotNull
    private Boolean status;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private Boolean canDelete;

}
