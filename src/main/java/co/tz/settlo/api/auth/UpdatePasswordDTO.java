package co.tz.settlo.api.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdatePasswordDTO(
        @NotNull
        @Schema(description = "password", example = "123456")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 32, message = "Password must be between 6 and 32 characters")
        String password,

        @NotNull
        @Schema(description = "Password reset token")
        UUID token
){}