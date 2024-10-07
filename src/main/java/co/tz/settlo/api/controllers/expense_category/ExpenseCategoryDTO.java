package co.tz.settlo.api.controllers.expense_category;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ExpenseCategoryDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    @ExpenseCategoryNameUnique
    private String name;

    @NotNull
    private Boolean status = true;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived = false;

    @NotNull
    private Boolean canDelete = true;

    @NotNull
    private UUID business;

    private UUID location;

}
