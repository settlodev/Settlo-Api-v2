package co.tz.settlo.api.controllers.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReservationDTO {

    private UUID id;

    @NotNull
    private OffsetDateTime date;

    @NotNull
    private OffsetDateTime startDate;

    @NotNull
    private OffsetDateTime endDate;

    @NotNull
    private Integer numberOfPeople;

    @NotNull
    @Size(max = 255)
    @ReservationNameUnique
    private String name;

    @NotNull
    @Size(max = 15)
    private String phone;

    @Size(max = 100)
    @ReservationEmailUnique
    private String email;

    @NotNull
    private Boolean status;

    @NotNull
    private Boolean canDelete;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    private UUID business;

    private UUID location;

    private UUID customer;

    private UUID product;

}
