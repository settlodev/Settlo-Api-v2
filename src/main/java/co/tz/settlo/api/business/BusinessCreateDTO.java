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

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;


    @Size(max = 100)
    private String businessType;

    @NotNull
    private UUID user;

    @NotNull
    private UUID country;

}
