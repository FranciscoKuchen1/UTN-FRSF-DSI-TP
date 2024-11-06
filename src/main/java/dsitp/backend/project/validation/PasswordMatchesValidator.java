package dsitp.backend.project.validation;

import dsitp.backend.project.model.BedelDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, BedelDTO> {

    @Override
    public boolean isValid(BedelDTO bedelDTO, ConstraintValidatorContext context) {
        return bedelDTO.getContrasena() != null && bedelDTO.getContrasena().equals(bedelDTO.getConfirmacionContrasena());
    }
}
