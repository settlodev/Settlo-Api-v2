package co.tz.settlo.api.controllers.customer;

import co.tz.settlo.api.common.models.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CustomerCreateDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String lastName;

    @NotNull
    // @Size(max = 255)
    private Gender gender;

    @NotNull
    @Size(max = 15)
    private String phoneNumber;

    @Size(max = 255)
    private String email;

    private UUID location;

    @NotNull
    private Boolean allowNotifications;

    private Boolean status;

    @JsonProperty("isArchived")
    private Boolean isArchived;

    private Boolean canDelete;

}
