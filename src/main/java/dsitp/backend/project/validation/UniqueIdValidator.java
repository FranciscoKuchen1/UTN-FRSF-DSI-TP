package dsitp.backend.project.validation;

import dsitp.backend.project.repos.BedelRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueIdValidator implements ConstraintValidator<UniqueId, Integer> {

    private final BedelRepository bedelRepository;

    @Autowired
    public UniqueIdValidator(BedelRepository bedelRepository) {
        this.bedelRepository = bedelRepository;
    }

    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext context) {
        return id != null && !bedelRepository.existsById(id);
    }
}
