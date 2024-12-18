package dsitp.backend.project.model;

import dsitp.backend.project.validation.CreateGroup;
import dsitp.backend.project.validation.UsuarioIdRegistroUnique;
import dsitp.backend.project.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministradorDTO {

    @Size(max = 20)
    @NotBlank(message = "El idRegistro es obligatorio.")
    @UsuarioIdRegistroUnique(groups = { CreateGroup.class })
    private String idRegistro;

    @Size(max = 100)
    @NotBlank(message = "El nombre es obligatorio.")
    private String nombre;

    @Size(max = 100)
    // TODO: ver si poner el 40?
    @NotBlank(message = "El apellido es obligatorio.")
    private String apellido;

    @Size(max = 100)
    @ValidPassword
    @NotBlank(message = "La contrasena es obligatoria.")
    private String contrasena;

}
