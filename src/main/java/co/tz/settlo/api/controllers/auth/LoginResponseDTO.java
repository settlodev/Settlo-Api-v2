package co.tz.settlo.api.controllers.auth;

import co.tz.settlo.api.common.models.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Schema(description = "Login response data transfer object")
public record LoginResponseDTO(
        @NotNull
        @Schema(description = "User's unique id")
        UUID id,

        @NotNull
        @Email(message = "Invalid email format")
        @Schema(description = "User's email address", example = "john.doe@qroo.co.tz")
        String email,

        @Schema(description = "User's first name", example = "John")
        String firstName,

        @Schema(description = "User's last name", example = "Doe")
        String lastName,

        @Schema(description = "User's profile picture url", example = "https://example.com/profile.jpg")
        String picture,

        @NotNull
        @Schema(description = "Phone number")
        String phoneNumber,

        @NotNull
        @Schema(description = "Authentication Token")
        String authToken,

        @NotNull
        @Schema(description = "Refresh token for obtaining a new access token")
        String refreshToken,

        @Schema(description = "If customer phone is verified")
        LocalDateTime phoneNumberVerified,

        @Schema(description = "If customer phone is verified")
        LocalDateTime emailVerified,

        @Schema(description = "Customer's cookie consent state")
        Boolean consent,

        @Schema(description = "Customer's preferred theme")
        String theme,

        @Schema(description = "Customer's business subscription status")
        SubscriptionStatus subscriptionStatus,

        @NotNull
        Boolean businessComplete,

        @NotNull
        Boolean locationComplete

) {
    public LoginResponseDTO {
        // Validate non-null fields
        if (id == null) throw new IllegalArgumentException("Could not find user with the requested id");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Could not find email address");
        if (authToken == null || authToken.isBlank()) throw new IllegalArgumentException("Could not generate authentication token");
        if (refreshToken == null || refreshToken.isBlank()) throw new IllegalArgumentException("Could not generate refresh token");

        // Trim string fields
        email = email.trim();
        if (firstName != null) firstName = firstName.trim();
        if (lastName != null) lastName = lastName.trim();
        if (picture != null) picture = picture.trim();
        if (theme != null) theme = theme.trim();
    }
}