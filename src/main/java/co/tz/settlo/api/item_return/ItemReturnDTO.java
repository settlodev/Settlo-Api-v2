package co.tz.settlo.api.item_return;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ItemReturnDTO {

    private UUID id;

    private String reason;

    @NotNull
    private OffsetDateTime dateOfReturn;

    @NotNull
    private Boolean status;

    @NotNull
    private Boolean canDelete;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private UUID orderItem;

    @NotNull
    private UUID staff;

    @NotNull
    private UUID approvedBy;

    @NotNull
    private UUID stockVariant;

}
