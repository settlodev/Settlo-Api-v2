package co.tz.settlo.api.controllers.brand;

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
        validatedBy = BrandNameUnique.BrandNameUniqueValidator.class
)
public @interface BrandNameUnique {

    String message() default "{Exists.brand.name}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class BrandNameUniqueValidator implements ConstraintValidator<BrandNameUnique, String> {

        private final BrandService brandService;
        private final HttpServletRequest request;

        public BrandNameUniqueValidator(final BrandService brandService,
                final HttpServletRequest request) {
            this.brandService = brandService;
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
            if (currentId != null && value.equalsIgnoreCase(brandService.get(UUID.fromString(currentId)).getName())) {
                // value hasn't changed
                return true;
            }
            return !brandService.nameExists(value);
        }

    }

}
