package dsitp.backend.project.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AulaInformaticaDTO {

    private Integer numero;

    @Size(max = 100)
    private String nombre;

    private Integer piso;

    private Integer capacidad;

    @Size(max = 100)
    private String tipoPizarron;

    private Boolean tieneAireAcondicionado;

    private Integer cantidadPCs;

    private Boolean tieneCanon;

}
