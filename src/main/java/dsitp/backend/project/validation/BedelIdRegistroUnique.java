package dsitp.backend.project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({FIELD, METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BedelIdRegistroUniqueValidator.class)
public @interface BedelIdRegistroUnique {

    String message() default "Ya existe bedel con mismo id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
