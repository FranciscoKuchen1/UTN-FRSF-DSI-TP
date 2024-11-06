package dsitp.backend.project.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DiaReservadoDTO {

    private Long id;

    private OffsetDateTime fechaReserva;

    @Schema(type = "string", example = "18:30")
    private LocalTime duracion;

    @Schema(type = "string", example = "18:30")
    private LocalTime horaInicio;

}
