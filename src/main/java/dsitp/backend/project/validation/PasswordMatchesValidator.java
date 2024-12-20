package dsitp.backend.project.validation;

import dsitp.backend.project.model.BedelDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, BedelDTO> {

    @Override
    public boolean isValid(BedelDTO bedelDTO, ConstraintValidatorContext context) {
        if (bedelDTO.getContrasena() == null) {
            return false;
        }

        if (!bedelDTO.getContrasena().equals(bedelDTO.getConfirmacionContrasena())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("La contraseña no cumple con las políticas de seguridad")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
