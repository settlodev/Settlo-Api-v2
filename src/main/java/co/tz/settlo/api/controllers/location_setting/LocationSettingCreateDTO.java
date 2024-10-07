package co.tz.settlo.api.controllers.location_setting;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
public class LocationSettingCreateDTO {
    private UUID id;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "69.08")
    private BigDecimal minimumSettlementAmount;

    @Column(nullable = false)
    private String systemPasscode;

    @Column(nullable = false)
    private String reportsPasscode;

    @NotNull
    private UUID locationId;

}
