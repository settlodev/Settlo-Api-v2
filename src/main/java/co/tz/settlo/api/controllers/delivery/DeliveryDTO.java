package co.tz.settlo.api.controllers.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DeliveryDTO {

    private UUID id;

    private String comment;

    @NotNull
    private Boolean status;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private Boolean canDelete;

    private UUID location;

    @NotNull
    private OrderStatus orderStatus;

    private UUID orderItem;

}
