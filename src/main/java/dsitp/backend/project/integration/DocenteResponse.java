package dsitp.backend.project.integration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DocenteResponse {

    private Integer id;
    private String nombre;
    private String apellido;
    private String correo;
}
