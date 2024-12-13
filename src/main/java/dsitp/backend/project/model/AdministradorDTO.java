package dsitp.backend.project.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministradorDTO {

    @Size(max = 20)
    private String idRegistro;

    @Size(max = 100)
    private String nombre;

    @Size(max = 100)
    private String apellido;

    @Size(max = 100)
    private String contrasena;

}
