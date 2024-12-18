package dsitp.backend.project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FutureMomentDTOValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureMomentDTO {

    String message() default "La fecha y hora especificadas deben ser futuras.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
