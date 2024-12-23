package dsitp.backend.project.model;

import dsitp.backend.project.validation.FutureMoment;
import dsitp.backend.project.validation.FutureMomentDTO;
import dsitp.backend.project.validation.ValidDuration;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FutureMomentDTO(message = "El momento debe ser futuro.")
public class DiaReservadoDTO {

    @NotNull(message = "La fecha de reserva es obligatoria.")
    private LocalDate fechaReserva;

    @Schema(type = "string", example = "18:30")
    @NotNull(message = "La hora de inicio es obligatoria.")
    private LocalTime horaInicio;

    @Schema(type = "string", example = "60")
    @ValidDuration
    @Positive
    @NotNull(message = "La duración es obligatoria.")
    private Integer duracion;

    private Integer idAula;

}
