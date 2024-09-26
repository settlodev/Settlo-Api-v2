package co.tz.settlo.api.sender_id;

import co.tz.settlo.api.location.Location;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SenderIdDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    @SenderIdNameUnique
    private String name;

    @NotNull
    private Boolean status;

    @NotNull
    private OffsetDateTime isArchived;

    @NotNull
    private OffsetDateTime canDelete;

    private UUID business;

    private Location location;

}
