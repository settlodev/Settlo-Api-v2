package co.tz.settlo.api.controllers.kyc;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KycDTO {

    private UUID id;

    @Size(max = 255)
    private String gender;

    private OffsetDateTime dateOfBirth;

    private Boolean canDelete = true;

    @JsonProperty("isArchived")
    private Boolean isArchived = false;

    private Boolean status = true;

    private UUID user;

}
