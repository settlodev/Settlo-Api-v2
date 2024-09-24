package co.tz.settlo.api.category;

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
 * Validate that the parentId value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = CategoryParentIdUnique.CategoryParentIdUniqueValidator.class
)
public @interface CategoryParentIdUnique {

    String message() default "{Exists.category.parentId}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class CategoryParentIdUniqueValidator implements ConstraintValidator<CategoryParentIdUnique, UUID> {

        private final CategoryService categoryService;
        private final HttpServletRequest request;

        public CategoryParentIdUniqueValidator(final CategoryService categoryService,
                final HttpServletRequest request) {
            this.categoryService = categoryService;
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
            if (currentId != null && value.equals(categoryService.get(UUID.fromString(currentId)).getParentId())) {
                // value hasn't changed
                return true;
            }
            return !categoryService.parentIdExists(value);
        }

    }

}
