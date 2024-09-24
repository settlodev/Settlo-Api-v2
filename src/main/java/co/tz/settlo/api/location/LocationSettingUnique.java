package co.tz.settlo.api.location;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the id value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = LocationSettingUnique.LocationSettingUniqueValidator.class
)
public @interface LocationSettingUnique {

    String message() default "{Exists.location.setting}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class LocationSettingUniqueValidator implements ConstraintValidator<LocationSettingUnique, UUID> {

        private final LocationService locationService;
        private final HttpServletRequest request;

        public LocationSettingUniqueValidator(final LocationService locationService,
                final HttpServletRequest request) {
            this.locationService = locationService;
            this.request = request;
        }

        @Override
        public boolean isValid(final UUID value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(locationService.get(UUID.fromString(currentId)).getSetting())) {
                // value hasn't changed
                return true;
            }
            return !locationService.settingExists(value);
        }

    }

}
