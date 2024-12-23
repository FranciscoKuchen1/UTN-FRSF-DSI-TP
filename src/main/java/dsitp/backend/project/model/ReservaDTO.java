package dsitp.backend.project.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaDTO {

    @NotNull(message = "El id de la cátedra es obligatorio.")
    private Integer idCatedra;

    @NotBlank(message = "El nombre de la cátedra es obligatorio.")
    @Size(max = 100, message = "El nombre de la cátedra no debe superar los 100 caracteres.")
    private String nombreCatedra;

    @NotNull(message = "El id del docente es obligatorio.")
    private Integer idDocente;

    @NotBlank(message = "El nombre del docente es obligatorio.")
    @Size(max = 100, message = "El nombre del docente no debe superar los 100 caracteres.")
    private String nombreDocente;

    @NotBlank(message = "El apellido del docente es obligatorio.")
    @Size(max = 100, message = "El apellido del docente no debe superar los 100 caracteres.")
    private String apellidoDocente;

    @NotBlank(message = "El correo del docente es obligatorio.")
    @Size(max = 100, message = "El correo del docente no debe superar los 100 caracteres.")
    @Email
    private String correoDocente;

    @Positive
    @NotNull(message = "La cantidad de alumnos es obligatoria.")
    @Max(value = 9999, message = "La cantidad de alumnos debe ser menor que 9999")
    private Integer cantAlumnos;

    @NotNull(message = "El tipo de aula es obligatorio.")
    private Integer tipoAula;

    @NotNull(message = "El tipo del periodo es obligatorio.")
    private Integer tipoPeriodo;

    @NotBlank(message = "El idRegistro del bedel es obligatorio.")
    @Size(max = 20, message = "El id de registro del bedel no debe superar los 100 caracteres.")
    private String idRegistroBedel;

    private List<DiaSemanaDTO> diasSemanaDTO;

    private List<DiaReservadoDTO> diasReservadosDTO;

}
