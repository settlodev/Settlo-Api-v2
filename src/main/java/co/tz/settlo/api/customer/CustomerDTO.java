package co.tz.settlo.api.customer;

import co.tz.settlo.api.common.models.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String lastName;

    @NotNull
    @Size(max = 255)
    private Gender gender;

    @NotNull
    @Size(max = 15)
    private String phoneNumber;

    @Size(max = 255)
    private String email;

    private UUID location;

    @NotNull
    private Boolean allowNotifications;

    @NotNull
    private Boolean status;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private Boolean canDelete;

}
