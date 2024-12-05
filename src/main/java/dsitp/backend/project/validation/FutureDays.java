package dsitp.backend.project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureDaysValidator.class)
public @interface FutureDays {

    String message() default "Los días deben ser posteriores a la fecha actual.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
