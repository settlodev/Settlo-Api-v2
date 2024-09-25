package co.tz.settlo.api.communication_log;

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

    @NotNull
    private Boolean status;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private Boolean canDelete;

    @NotNull
    private UUID campaign;

    private UUID location;
}
