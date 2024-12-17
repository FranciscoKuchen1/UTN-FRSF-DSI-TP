package dsitp.backend.project.model;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DiaSemanaDTO {

    @NotNull
    private Integer dia;
    @NotBlank
    private String horaInicio;
    @NotBlank
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
