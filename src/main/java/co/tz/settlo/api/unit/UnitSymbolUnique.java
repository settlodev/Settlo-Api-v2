package co.tz.settlo.api.unit;

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
 * Validate that the symbol value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = UnitSymbolUnique.UnitSymbolUniqueValidator.class
)
public @interface UnitSymbolUnique {

    String message() default "{Exists.unit.symbol}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class UnitSymbolUniqueValidator implements ConstraintValidator<UnitSymbolUnique, String> {

        private final UnitService unitService;
        private final HttpServletRequest request;

        public UnitSymbolUniqueValidator(final UnitService unitService,
                final HttpServletRequest request) {
            this.unitService = unitService;
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
            if (currentId != null && value.equalsIgnoreCase(unitService.get(UUID.fromString(currentId)).getSymbol())) {
                // value hasn't changed
                return true;
            }
            return !unitService.symbolExists(value);
        }

    }

}
