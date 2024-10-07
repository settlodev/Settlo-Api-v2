package co.tz.settlo.api.controllers.subscription;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SubscriptionDTO {

    private UUID id;

    @NotNull
    private Double amount;

    private Double discount;

    @NotNull
    @Size(max = 255)
    private String packageName;

    @NotNull
    @Size(max = 255)
    @SubscriptionPackageCodeUnique
    private String packageCode;

    @NotNull
    @JsonProperty("isTrial")
    private Boolean isTrial;

    @NotNull
    private Boolean status;

    @NotNull
    private Boolean canDelete;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

}
