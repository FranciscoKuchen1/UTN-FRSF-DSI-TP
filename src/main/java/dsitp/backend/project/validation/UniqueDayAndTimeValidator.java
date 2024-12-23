package dsitp.backend.project.validation;

import dsitp.backend.project.domain.DiaReservado;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class UniqueDayAndTimeValidator implements ConstraintValidator<UniqueDayAndTime, List<DiaReservado>> {

    @Override
    public boolean isValid(List<DiaReservado> diasReservados, ConstraintValidatorContext context) {
        Boolean isValid = true;
        if (diasReservados == null || diasReservados.isEmpty()) {
            return isValid;
        }

        for (DiaReservado dia : diasReservados) {
            int count = 0;
            for (DiaReservado dia2 : diasReservados) {
                if (dia.getFechaReserva().isEqual(dia2.getFechaReserva())) {
                    count++;
                }
            }
            if (count > 1) {
                isValid = false;
            }
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Existe más de una hora de inicio y duración para el mismo día.")
                    .addConstraintViolation();
        }
        return isValid;
    }
}