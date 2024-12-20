package dsitp.backend.project.model;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodoDTO {

    private Integer tipoPerido;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

}
