package co.tz.settlo.api.controllers.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class SupplierRequestDTO {

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

    private Boolean status;

    private Boolean canDelete;

    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private UUID business;

    private UUID location;

}
