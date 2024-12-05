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
        if (diasReservados == null || diasReservados.isEmpty()) {
            return true;
        }

        Map<LocalDate, Set<LocalTime>> diasMap = new HashMap<>();
        for (DiaReservado dia : diasReservados) {
            diasMap.computeIfAbsent(dia.getFechaReserva(), k -> new HashSet<>()).add(dia.getHoraInicio());
        }

        boolean isValid = diasMap.values().stream().allMatch(set -> set.size() == 1);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Existen horarios duplicados en los días reservados.")
                    .addConstraintViolation();
        }
        return isValid;
    }
}
