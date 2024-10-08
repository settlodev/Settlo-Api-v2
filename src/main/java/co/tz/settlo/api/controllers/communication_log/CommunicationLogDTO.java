package co.tz.settlo.api.controllers.communication_log;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommunicationLogDTO {

    private UUID id;

    @NotNull
    private Integer totalAudience;

    @NotNull
    private Integer totalSuccessful;

    @NotNull
    private Integer totalFailed;

    private Boolean status = true;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived = false;

    private Boolean canDelete = true;

    @NotNull
    private UUID campaign;

    private UUID location;
}
