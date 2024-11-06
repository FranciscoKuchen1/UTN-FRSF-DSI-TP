package dsitp.backend.project.validation;

import org.springframework.stereotype.Component;

@Component
public class PasswordPolicyValidator {

    public boolean isValid(String password) {

        return true;
    }
}
