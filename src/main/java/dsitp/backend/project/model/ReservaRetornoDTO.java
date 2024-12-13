package dsitp.backend.project.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaRetornoDTO {

    private List<DiaDisponibilidadDTO> diasDisponibles;
    private List<DiaSolapamientoDTO> diasConSolapamiento;

}
