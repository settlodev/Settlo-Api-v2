package co.tz.settlo.api.auth;

import co.tz.settlo.api.common.models.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
class UserRegistrationStatus {
    Boolean hasBusinessCreated;

    Boolean hasLocationCreated;
}