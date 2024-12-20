package dsitp.backend.project.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class DurationValidatorDTO implements ConstraintValidator<ValidDurationDTO, String> {

    @Override
    public boolean isValid(String duracion, ConstraintValidatorContext context) {
        if (duracion == null) {
            return false;
        }

        Integer duracionInteger = Integer.valueOf(duracion);

        if (duracionInteger % 30 != 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("La duración no es un múltiplo de 30 minutos.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
