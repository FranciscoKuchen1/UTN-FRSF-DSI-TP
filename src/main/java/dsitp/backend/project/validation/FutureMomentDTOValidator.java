package dsitp.backend.project.validation;

import dsitp.backend.project.domain.DiaReservado;
import dsitp.backend.project.model.DiaReservadoDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FutureMomentDTOValidator implements ConstraintValidator<FutureMomentDTO, DiaReservadoDTO> {

    @Override
    public boolean isValid(DiaReservadoDTO diaReservadoDTO, ConstraintValidatorContext context) {
        if (diaReservadoDTO == null) {
            return true;
        }

        LocalDate fechaReserva = diaReservadoDTO.getFechaReserva();
        LocalTime horaInicio = diaReservadoDTO.getHoraInicio();

        if (fechaReserva == null || horaInicio == null) {
            return false;
        }

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime momentoReservado = LocalDateTime.of(fechaReserva, horaInicio);

        return momentoReservado.isAfter(ahora);
    }
}
