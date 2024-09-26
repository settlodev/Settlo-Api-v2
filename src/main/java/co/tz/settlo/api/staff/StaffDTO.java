package co.tz.settlo.api.staff;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StaffDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 20)
    private String phone;

    @Size(max = 10)
    private String color;

    @Size(max = 255)
    private String image;

    @NotNull
    @Size(max = 255)
    private String passCode;

    @NotNull
    private Boolean canDelete;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    private Boolean status;

    @NotNull
    private UUID role;

    @NotNull
    private UUID business;

    @NotNull
    private UUID department;

    private UUID salary;

    private UUID location;

}
