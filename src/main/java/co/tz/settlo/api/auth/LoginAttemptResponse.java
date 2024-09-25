package co.tz.settlo.api.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LoginAttemptResponse (
        UUID id,

        @NotNull
        @Size(max = 100)
        String email,

        @NotNull
        @Schema(description = "The login status")
        boolean success,

        @NotNull
        @Schema(description = "The date and time of the login attempt")
        OffsetDateTime dateCreated
){
    public static LoginAttemptResponse convertToDTO(LoginAttempt loginAttempt) {
        return new LoginAttemptResponse(loginAttempt.getId(), loginAttempt.getEmail(), loginAttempt.getSuccess(),  loginAttempt.getDateCreated());

    }
}
