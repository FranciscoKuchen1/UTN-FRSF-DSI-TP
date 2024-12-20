package dsitp.backend.project.validation;

import dsitp.backend.project.model.DiaReservadoDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FutureDaysValidator implements ConstraintValidator<FutureDays, List<DiaReservadoDTO>> {

    @Override
    public boolean isValid(List<DiaReservadoDTO> diasReservados, ConstraintValidatorContext context) {
        if (diasReservados == null || diasReservados.isEmpty()) {
            return false;
        }

        LocalDate now = LocalDate.now();
        for (DiaReservadoDTO dia : diasReservados) {
            if (dia.getFechaReserva() != null && !dia.getFechaReserva().isAfter(now)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Hay días que no son posteriores a la fecha actual.")
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
