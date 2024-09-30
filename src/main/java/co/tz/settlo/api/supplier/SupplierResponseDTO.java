package co.tz.settlo.api.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class SupplierResponseDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    @SupplierNameUnique
    private String name;

    @NotNull
    @Size(max = 255)
    private String phoneNumber;

    @Size(max = 255)
    private String email;

    @NotNull
    private Boolean status;

    @NotNull
    private Boolean canDelete;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private UUID business;

    private UUID location;

}
