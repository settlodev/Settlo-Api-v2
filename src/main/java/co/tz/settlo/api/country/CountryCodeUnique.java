package co.tz.settlo.api.country;

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
 * Validate that the code value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = CountryCodeUnique.CountryCodeUniqueValidator.class
)
public @interface CountryCodeUnique {

    String message() default "{Exists.country.code}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CountryCodeUniqueValidator implements ConstraintValidator<CountryCodeUnique, String> {

        private final CountryService countryService;
        private final HttpServletRequest request;

        public CountryCodeUniqueValidator(final CountryService countryService,
                final HttpServletRequest request) {
            this.countryService = countryService;
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
            if (currentId != null && value.equalsIgnoreCase(countryService.get(UUID.fromString(currentId)).getCode())) {
                // value hasn't changed
                return true;
            }
            return !countryService.codeExists(value);
        }

    }

}
