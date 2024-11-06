package dsitp.backend.project.model;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PeriodoDTO {

    private Integer id;
    private OffsetDateTime fechaInicio;
    private OffsetDateTime fechaFin;

}
