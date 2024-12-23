package dsitp.backend.project.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaReservadoDisponibilidadDTO {

    private DiaReservadoDTO diaReservado;
    private List<AulaDTO> aulasDisponibles;

}
