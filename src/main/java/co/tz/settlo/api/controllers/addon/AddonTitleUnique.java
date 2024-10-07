package co.tz.settlo.api.controllers.addon;

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
 * Validate that the title value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = AddonTitleUnique.AddonTitleUniqueValidator.class
)
public @interface AddonTitleUnique {

    String message() default "{Exists.addon.title}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class AddonTitleUniqueValidator implements ConstraintValidator<AddonTitleUnique, String> {

        private final AddonService addonService;
        private final HttpServletRequest request;

        public AddonTitleUniqueValidator(final AddonService addonService,
                final HttpServletRequest request) {
            this.addonService = addonService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(addonService.get(UUID.fromString(currentId)).getTitle())) {
                // value hasn't changed
                return true;
            }
            return !addonService.titleExists(value);
        }

    }

}
