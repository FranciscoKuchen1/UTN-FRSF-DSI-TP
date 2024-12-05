package dsitp.backend.project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueDayAndTimeValidator.class)
public @interface UniqueDayAndTime {

    String message() default "Existe más de una hora de inicio y duración para el mismo día.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
