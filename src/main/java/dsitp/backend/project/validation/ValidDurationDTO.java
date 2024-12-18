package dsitp.backend.project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DurationValidatorDTO.class)
public @interface ValidDurationDTO {

    String message() default "La duración debe ser un múltiplo de 30 minutos.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
