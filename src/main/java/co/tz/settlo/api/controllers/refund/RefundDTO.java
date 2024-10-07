package co.tz.settlo.api.controllers.refund;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RefundDTO {

    private UUID id;

    @NotNull
    private OffsetDateTime dateOfRefund;

    @NotNull
    private String reason;

    @NotNull
    private Boolean status;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private Boolean canDelete;

    @NotNull
    private UUID orderItem;

    @NotNull
    private UUID staff;

    @NotNull
    private UUID approvedBy;

    @NotNull
    private UUID stockVariant;

    private UUID locationId;

}
