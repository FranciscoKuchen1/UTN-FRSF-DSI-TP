package dsitp.backend.project.model;

import java.util.List;

import dsitp.backend.project.validation.ValidDurationDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DiaSemanaDTO {

    @NotNull(message = "El dia de la semana es obligatorio.")
    private Integer dia;

    @NotBlank(message = "La hora de inicio es obligatoria.")
    @Pattern(regexp = "^[0-9]{2}:[0-9]{2}$", message = "El formato de la hora debe ser HH:mm, usando solo dígitos")
    private String horaInicio;

    @ValidDurationDTO
    @Positive
    @NotBlank(message = "La duración es obligatoria.")
    @Pattern(regexp = "^[0-9]+$", message = "La duración debe contener solo dígitos")
    private String duracion;

    private Integer idAula;

    public static Boolean containsFirst(List<DiaSemanaDTO> diasSemanaDTO, Integer target) {
        return diasSemanaDTO.stream()
                .anyMatch(trio -> trio.getDia().equals(target));
    }

    public DiaSemanaDTO findByFirst(List<DiaSemanaDTO> list, Integer target) {
        return list.stream()
                .filter(trio -> trio.getDia().equals(target))
                .findFirst()
                .orElse(null);
    }

}
