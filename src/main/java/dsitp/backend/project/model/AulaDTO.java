package dsitp.backend.project.model;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AulaDTO {

    private Integer numero;

    private String nombre;

    private Integer piso;

    private Integer capacidad;

    private Integer tipoPizarron;

    private Boolean tieneAireAcondicionado;

    private Integer tipoAula;

    private Map<String, Object> atributosEspecificos;

}
