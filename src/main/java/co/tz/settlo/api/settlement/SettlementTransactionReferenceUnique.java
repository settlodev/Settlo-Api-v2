package co.tz.settlo.api.settlement;

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
 * Validate that the transactionReference value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = SettlementTransactionReferenceUnique.SettlementTransactionReferenceUniqueValidator.class
)
public @interface SettlementTransactionReferenceUnique {

    String message() default "{Exists.settlement.transactionReference}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class SettlementTransactionReferenceUniqueValidator implements ConstraintValidator<SettlementTransactionReferenceUnique, String> {

        private final SettlementService settlementService;
        private final HttpServletRequest request;

        public SettlementTransactionReferenceUniqueValidator(
                final SettlementService settlementService, final HttpServletRequest request) {
            this.settlementService = settlementService;
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
            if (currentId != null && value.equalsIgnoreCase(settlementService.get(UUID.fromString(currentId)).getTransactionReference())) {
                // value hasn't changed
                return true;
            }
            return !settlementService.transactionReferenceExists(value);
        }

    }

}
