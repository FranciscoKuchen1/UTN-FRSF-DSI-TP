package dsitp.backend.project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordFormatValidator.class)
public @interface ValidPassword {

    String message() default "La contraseña no cumple con las políticas de seguridad";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
