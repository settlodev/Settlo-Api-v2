package co.tz.settlo.api.controllers.campaign;

import co.tz.settlo.api.controllers.communication_template.BroadcastType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CampaignDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    @CampaignNameUnique
    private String name;

    @NotNull
    private String message;

    @NotNull
    private Boolean status;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private Boolean canDelete;

    @NotNull
    private Recepient audience;

    @Size(max = 255)
    private String customMessage;

    private BroadcastType broadcastType;

    private UUID senderId;

    private UUID communicationTemplate;

    @NotNull
    private UUID business;

    private UUID location;
}
