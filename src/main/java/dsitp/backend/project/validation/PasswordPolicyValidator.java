package dsitp.backend.project.validation;

import dsitp.backend.project.service.PasswordExternalApiService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordPolicyValidator implements ConstraintValidator<ValidPassword, String> {

    private final PasswordExternalApiService passwordExternalApiService;

    @Autowired
    public PasswordPolicyValidator(PasswordExternalApiService passwordExternalApiService) {
        this.passwordExternalApiService = passwordExternalApiService;
    }

    @Override
    public boolean isValid(String contrasena, ConstraintValidatorContext context) {
        if (contrasena == null) {
            return false;
        }

        List<String> errores = new ArrayList<>();

        Boolean isValid = true;

        if (contrasena.length() < 6) {
            isValid = false;
            errores.add("La contraseña debe tener al menos 6 caracteres.");
        }

        if (!contrasena.matches(".*[@#$%&*].*")) {
            isValid = false;
            errores.add("La contraseña debe contener al menos un signo especial (@#$%&*).");
        }

        if (!contrasena.matches(".*[A-Z].*")) {
            isValid = false;
            errores.add("La contraseña debe contener al menos una letra mayúscula.");
        }

        if (!contrasena.matches(".*\\d.*")) {
            isValid = false;
            errores.add("La contraseña debe contener al menos un dígito.");
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("La contraseña no cumple con las políticas de seguridad")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
