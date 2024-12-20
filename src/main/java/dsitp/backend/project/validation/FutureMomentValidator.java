package dsitp.backend.project.validation;

import dsitp.backend.project.domain.DiaReservado;
import dsitp.backend.project.model.DiaReservadoDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FutureMomentValidator implements ConstraintValidator<FutureMoment, DiaReservado> {

    @Override
    public boolean isValid(DiaReservado diaReservado, ConstraintValidatorContext context) {
        if (diaReservado == null) {
            return true;
        }

        LocalDate fechaReserva = diaReservado.getFechaReserva();
        LocalTime horaInicio = diaReservado.getHoraInicio();

        if (fechaReserva == null || horaInicio == null) {
            return false;
        }

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime momentoReservado = LocalDateTime.of(fechaReserva, horaInicio);

        return momentoReservado.isAfter(ahora);
    }
}
