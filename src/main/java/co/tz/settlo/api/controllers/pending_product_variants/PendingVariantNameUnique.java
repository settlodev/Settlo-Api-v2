package co.tz.settlo.api.controllers.pending_product_variants;

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
 * Validate that the name value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = PendingVariantNameUnique.VariantNameUniqueValidator.class
)
public @interface PendingVariantNameUnique {

    String message() default "{Exists.variant.name}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class VariantNameUniqueValidator implements ConstraintValidator<PendingVariantNameUnique, String> {

        private final PendingVariantService pendingVariantService;
        private final HttpServletRequest request;

        public VariantNameUniqueValidator(final PendingVariantService pendingVariantService,
                final HttpServletRequest request) {
            this.pendingVariantService = pendingVariantService;
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
            if (currentId != null && value.equalsIgnoreCase(pendingVariantService.get(UUID.fromString(currentId)).getName())) {
                // value hasn't changed
                return true;
            }
            return !pendingVariantService.nameExists(value);
        }

    }

}
