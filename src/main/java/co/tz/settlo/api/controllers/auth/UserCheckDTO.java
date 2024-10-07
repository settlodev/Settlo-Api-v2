package co.tz.settlo.api.controllers.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserCheckDTO {
    private String username;

    @NotNull
    private LocalDateTime emailVerified;

    @NotNull
    private LocalDateTime phoneVerified;

    @NotNull
    private String message;

    @NotNull
    private Integer responseCode;
}
