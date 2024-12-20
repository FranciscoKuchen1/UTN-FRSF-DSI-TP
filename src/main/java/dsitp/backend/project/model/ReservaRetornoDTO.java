package dsitp.backend.project.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaRetornoDTO {

    private List<DiaReservadoDisponibilidadDTO> diasReservadosDisponibles;
    private List<DiaReservadoConSolapamientoDTO> diasReservadosConSolapamiento;
    private List<DiaSemanaDisponibilidadDTO> diasSemanaDisponibles;
    private List<DiaSemanaConSolapamientoDTO> diasSemanaConSolapamiento;

}
