package co.tz.settlo.api.shift_log;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ShiftLogDTO {

    private UUID id;

    @NotNull
    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

    @NotNull
    private Boolean status;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private Boolean canDelete;

    @NotNull
    private UUID shift;

    @NotNull
    private UUID staff;

}
