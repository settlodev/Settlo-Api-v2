package co.tz.settlo.api.stock_variant;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StockVariantDTO {

    private UUID id;

    @NotNull
    private Integer startingValue;

    @NotNull
    private Integer actualValue;

    @NotNull
    private Integer value;

    @NotNull
    private Integer startingQuantity;

    @NotNull
    private Integer actualQuantity;

    @NotNull
    private Integer quantity;

    @Size(max = 255)
    private String variantName;

    private Integer alertLevel;

    @Size(max = 255)
    private String imageOption;

    @NotNull
    private Boolean status;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private Boolean canDelete;

    private UUID stock;

    private UUID location;

}
