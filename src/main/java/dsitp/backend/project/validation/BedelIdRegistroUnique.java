package dsitp.backend.project.validation;

import dsitp.backend.project.service.BedelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;

@Target({FIELD, METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = BedelIdRegistroUnique.BedelIdRegistroUniqueValidator.class
)
public @interface BedelIdRegistroUnique {

    String message() default "Ya existe bedel con mismo id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class BedelIdRegistroUniqueValidator implements ConstraintValidator<BedelIdRegistroUnique, String> {

        private final BedelService bedelService;
        private final HttpServletRequest request;

        public BedelIdRegistroUniqueValidator(final BedelService bedelService,
                final HttpServletRequest request) {
            this.bedelService = bedelService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {

                return true;
            }
            @SuppressWarnings("unchecked")
            final Map<String, String> pathVariables
                    = ((Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(bedelService.get(Integer.valueOf(currentId)).getIdRegistro())) {

                return true;
            }
            return !bedelService.idRegistroExists(value);
        }

    }

}
