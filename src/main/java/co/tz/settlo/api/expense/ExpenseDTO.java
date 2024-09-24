package co.tz.settlo.api.expense;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ExpenseDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    @ExpenseNameUnique
    private String name;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "92.08")
    private BigDecimal amount;

    @NotNull
    private Boolean status;

    @NotNull
    @Size(max = 255)
    private String isArchived;

    @NotNull
    private Boolean canDelete;

    private UUID expenseCategory;

    @NotNull
    private UUID business;

}
