package dsitp.backend.project.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaSolapadaDTO {

    private String nombreCatedra;
    private String nombreDocente;
    private String apellidoDocente;
    private String correoDocente;
    private LocalTime inicioReserva;
    private LocalTime finReserva;

}
