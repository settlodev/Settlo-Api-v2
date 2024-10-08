package co.tz.settlo.api.product;

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
        validatedBy = ProductNameUnique.ProductNameUniqueValidator.class
)
public @interface ProductNameUnique {

    String message() default "{Exists.product.name}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ProductNameUniqueValidator implements ConstraintValidator<ProductNameUnique, String> {

        private final ProductService productService;
        private final HttpServletRequest request;

        public ProductNameUniqueValidator(final ProductService productService,
                final HttpServletRequest request) {
            this.productService = productService;
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
            if (currentId != null && value.equalsIgnoreCase(productService.get(UUID.fromString(currentId)).getName())) {
                // value hasn't changed
                return true;
            }
            return !productService.nameExists(value);
        }

    }

}
