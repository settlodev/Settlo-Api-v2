package co.tz.settlo.api.controllers.location;

import co.tz.settlo.api.common.models.SubscriptionStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;


@Getter
@Setter
public class LocationResponseDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 20)
    private String phone;

    @NotNull
    @Size(max = 255)
    private String email;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String region;

    @Size(max = 100)
    private String street;

    @Size(max = 255)
    private String address;

    private String description;

    @NotNull
    @Size(max = 10)
    private String openingTime;

    @NotNull
    @Size(max = 10)
    private String closingTime;

    @NotNull
    private Boolean status;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private Boolean canDelete;

    @NotNull
    @LocationSettingUnique
    private UUID setting;

    @NotNull
    private UUID business;

    private OffsetDateTime endDate;

    private SubscriptionStatus subscriptionStatus;
}
