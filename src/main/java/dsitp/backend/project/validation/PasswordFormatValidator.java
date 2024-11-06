package dsitp.backend.project.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordFormatValidator implements ConstraintValidator<ValidPassword, String> {

    private final PasswordPolicyValidator passwordPolicyValidator;

    @Autowired
    public PasswordFormatValidator(PasswordPolicyValidator passwordPolicyValidator) {
        this.passwordPolicyValidator = passwordPolicyValidator;
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && passwordPolicyValidator.isValid(password);
    }
}
