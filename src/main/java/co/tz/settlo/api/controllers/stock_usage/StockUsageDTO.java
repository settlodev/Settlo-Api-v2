package co.tz.settlo.api.controllers.stock_usage;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StockUsageDTO {

    private UUID id;

    @NotNull
    private Double quantity;

    @NotNull
    private Boolean status;

    @NotNull
    private Boolean canDelete;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private ItemType itemType;

    @NotNull
    private UUID itemId;

    private UUID unit;

    @NotNull
    private UUID business;

    @NotNull
    private UUID location;

}
