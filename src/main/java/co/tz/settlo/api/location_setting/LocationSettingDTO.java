package co.tz.settlo.api.location_setting;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LocationSettingDTO {

    private UUID id;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "69.08")
    private BigDecimal minimumSettlementAmount;

    @NotNull
    @Size(max = 255)
    private String systemPasscode;

    @NotNull
    @Size(max = 255)
    private String reportsPasscode;

    @NotNull
    @JsonProperty("isDefault")
    private Boolean isDefault;

    @NotNull
    private Boolean trackInventory;

    @NotNull
    private Boolean ecommerceEnabled;

    @NotNull
    private Boolean enableNotifications;

    @NotNull
    private Boolean useRecipe;

    @NotNull
    private Boolean useDepartments;

    @NotNull
    private Boolean useCustomPrice;

    @NotNull
    private Boolean useWarehouse;

    @NotNull
    private Boolean useShifts;

    @NotNull
    private Boolean useKds;

    @NotNull
    private UUID locationId;
    
    @NotNull
    @JsonProperty("isActive")
    private Boolean isActive;

    @NotNull
    private Boolean canDelete;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

}
