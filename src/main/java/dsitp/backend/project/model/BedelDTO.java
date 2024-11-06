package dsitp.backend.project.model;

import dsitp.backend.project.validation.PasswordMatches;
import dsitp.backend.project.validation.UniqueId;
import dsitp.backend.project.validation.ValidPassword;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@PasswordMatches
@Getter
@Setter
public class BedelDTO {

    @UniqueId
    private Integer id;

    @Size(max = 100)
    private String nombre;

    @Size(max = 100)
    private String apellido;

    @Size(max = 100)
    @ValidPassword
    private String contrasena;

    @Size(max = 100)
    private String confirmacionContrasena;

    private Integer tipoTurno;

}
