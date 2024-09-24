package co.tz.settlo.api.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private UUID id;

    @NotNull
    @UserPrefixUnique
    private Integer prefix;

    @NotNull
    @Size(max = 100)
    private String firstName;

    @NotNull
    @Size(max = 100)
    private String lastName;

    @Size(max = 255)
    private String avatar;

    @NotNull
    @Size(max = 255)
    @UserEmailUnique
    private String email;

    @Size(max = 255)
    private String companyName;

    @Size(max = 255)
    private String slug;

    @NotNull
    @Size(max = 255)
    private String phone;

    @NotNull
    private Boolean verificationStatus;

    @Size(max = 255)
    private String region;

    @Size(max = 255)
    private String district;

    @Size(max = 255)
    private String ward;

    @Size(max = 10)
    private String areaCode;

    @Size(max = 100)
    private String identificationId;

    @Size(max = 255)
    private String municipal;

    @NotNull
    private Boolean status;

    @NotNull
    @JsonProperty("isArchived")
    private Boolean isArchived;

    @NotNull
    @JsonProperty("isOwner")
    private Boolean isOwner;

    @NotNull
    private Boolean canDelete;

    @NotNull
    private UUID country;

}
