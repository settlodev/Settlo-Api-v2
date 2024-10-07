package co.tz.settlo.api.controllers.discount;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DiscountDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    @DiscountNameUnique
    private String name;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "32.08")
    private BigDecimal discountValue;

    @NotNull
    private OffsetDateTime validFrom;

    @NotNull
    private OffsetDateTime validTo;

    @NotNull
    @Size(max = 255)
    @DiscountDiscountCodeUnique
    private String discountCode;

    private Integer minimumSpend;

    @NotNull
    private DiscountType discountType;

    @NotNull
    private Integer usageLimit;

    @NotNull
    private Integer activations;

    @NotNull
    private Boolean status;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private Boolean canDelete;

    private UUID department;

    private UUID customer;

    private UUID location;

    private UUID category;

}
