package co.tz.settlo.api.controllers.discount;

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
 * Validate that the discountCode value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = DiscountDiscountCodeUnique.DiscountDiscountCodeUniqueValidator.class
)
public @interface DiscountDiscountCodeUnique {

    String message() default "{Exists.discount.discountCode}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class DiscountDiscountCodeUniqueValidator implements ConstraintValidator<DiscountDiscountCodeUnique, String> {

        private final DiscountService discountService;
        private final HttpServletRequest request;

        public DiscountDiscountCodeUniqueValidator(final DiscountService discountService,
                final HttpServletRequest request) {
            this.discountService = discountService;
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
            if (currentId != null && value.equalsIgnoreCase(discountService.get(UUID.fromString(currentId)).getDiscountCode())) {
                // value hasn't changed
                return true;
            }
            return !discountService.discountCodeExists(value);
        }

    }

}
