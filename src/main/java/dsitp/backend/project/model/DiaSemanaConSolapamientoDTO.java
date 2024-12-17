package dsitp.backend.project.model;

import java.util.List;

import dsitp.backend.project.util.Trio;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaSemanaConSolapamientoDTO {

    private DiaSemanaDTO diaSemana;
    private List<AulaSolapadaDTO> aulasConSolapamiento;

}
