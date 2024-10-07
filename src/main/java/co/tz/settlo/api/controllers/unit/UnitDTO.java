package co.tz.settlo.api.controllers.unit;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UnitDTO {

    private UUID id;

    @NotNull
    @Size(max = 20)
    @UnitNameUnique
    private String name;

    @NotNull
    @Size(max = 10)
    @UnitSymbolUnique
    private String symbol;

    @NotNull
    private Boolean status;

    @NotNull
    private Boolean canDelete;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

}
