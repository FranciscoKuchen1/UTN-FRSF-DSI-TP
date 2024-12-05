package dsitp.backend.project.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaRespuestaDTO {

    private List<DiaDisponibilidadDTO> diasDisponibles; // Para días exitosos
    private List<DiaSolapamientoDTO> diasConSolapamiento; // Para días con solapamientos

}
