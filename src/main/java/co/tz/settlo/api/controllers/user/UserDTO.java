package co.tz.settlo.api.controllers.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private UUID id;

    @UserAccountNumberUnique
    private String accountNumber;

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

    @Size(max = 100)
    private String slug;

    @NotNull
    @Size(max = 20)
    private String phoneNumber;

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

    private LocalDateTime emailVerified;

    private Boolean consent;

    private String theme;

    @NotNull
    @Size(max = 255)
    private String password;

    private UUID role;

    private LocalDateTime phoneNumberVerified;

    @NotNull
    private Boolean status;

    @NotNull
    private Boolean isArchived;

    @NotNull
    private Boolean isOwner;

    @NotNull
    private Boolean canDelete;

    @NotNull
    private UUID country;

}
