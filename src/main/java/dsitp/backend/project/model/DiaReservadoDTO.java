package dsitp.backend.project.model;

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
public class DiaReservadoDTO {

//    @NotNull(message = "El id del dia reservado es obligatorio.")
//    private Integer id;
    @Future(message = "La fecha debe ser en el futuro.")
    @NotNull(message = "La fecha de reserva es obligatoria.")
    private LocalDate fechaReserva;

    @Schema(type = "string", example = "60")
    @ValidDuration
    @Positive
    @NotNull(message = "La duración es obligatoria.")
    // TODO: ver tipo de dato, si cambiar?
    private Integer duracion;

    @Schema(type = "string", example = "18:30")
    @NotNull(message = "La hora de inicio es obligatoria.")
    private LocalTime horaInicio;

    @NotNull(message = "El id del aula es obligatorio.")
    // TODO: ver de donde obtener
    private Integer aulaId;

}
