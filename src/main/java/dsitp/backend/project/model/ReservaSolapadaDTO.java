package dsitp.backend.project.model;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaSolapadaDTO {

    private String nombreCatedra;
    private String nombreDocente;
    private String apellidoDocente;
    private String correoDocente;
    private LocalDate inicioReserva;
    private LocalDate finReserva;

}
