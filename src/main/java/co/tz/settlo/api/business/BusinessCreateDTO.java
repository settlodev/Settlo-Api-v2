package co.tz.settlo.api.business;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class BusinessCreateDTO {
    private UUID id;

    @Size(max = 50)
    private String prefix;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 100)
    private String slug;

    private String description;

    @NotNull
    @Size(max = 255)
    private String notificationEmailAddress;

    @NotNull
    private Boolean vfdRegistrationState;

    @NotNull
    private UUID user;

    @NotNull
    private UUID country;

}
