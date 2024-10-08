package co.tz.settlo.api.controllers.country;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CountryDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    @CountryNameUnique
    private String name;

    @NotNull
    @Size(max = 20)
    @CountryCodeUnique
    private String code;

    @NotNull
    @Size(max = 10)
    private String locale;

    @NotNull
    @Size(max = 5)
    private String currencyCode;

    @NotNull
    private Boolean supported;

    private Boolean status = true;

    private Boolean canDelete = true;

    @JsonProperty("isArchived")
    private Boolean isArchived = false;

}
