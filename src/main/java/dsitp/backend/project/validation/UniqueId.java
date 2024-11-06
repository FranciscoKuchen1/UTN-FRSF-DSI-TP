package dsitp.backend.project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueIdValidator.class)
public @interface UniqueId {

    String message() default "Existe otro bedel con el mismo id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
