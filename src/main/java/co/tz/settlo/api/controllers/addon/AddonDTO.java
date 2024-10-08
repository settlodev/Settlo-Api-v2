package co.tz.settlo.api.controllers.addon;

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
public class AddonDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    @AddonTitleUnique
    private String title;

    private Boolean status = true;

    private Boolean canDelete = true;

    @JsonProperty("isArchived")
    private Boolean isArchived = false;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "75.08")
    private BigDecimal price;

    private UUID orderItem;

    private UUID location;
}
