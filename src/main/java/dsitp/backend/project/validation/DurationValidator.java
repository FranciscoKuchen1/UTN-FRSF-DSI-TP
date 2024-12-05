package dsitp.backend.project.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class DurationValidator implements ConstraintValidator<ValidDuration, Integer> {

    @Override
    public boolean isValid(Integer duracion, ConstraintValidatorContext context) {
        if (duracion == null) {
            return true;
        }

        if (duracion % 30 != 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("La duración no es un múltiplo de 30 minutos.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
