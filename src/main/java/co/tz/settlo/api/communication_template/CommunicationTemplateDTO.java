package co.tz.settlo.api.communication_template;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommunicationTemplateDTO {

    private UUID id;

    @NotNull
    private String message;

    @NotNull
    private Boolean status;

    @NotNull
    @Size(max = 255)
    private String subject;

    @NotNull
    private Boolean canDelete;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    private BroadcastType broadcastType;

}
