package dsitp.backend.project.util;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Trio<T1, T2, T3> {

    private T1 dia;
    private T2 horaInicio;
    private T3 duracion;

    public static Boolean containsFirst(List<Trio<Integer, String, String>> list, Integer target) {
        return list.stream()
                .anyMatch(trio -> trio.getDia().equals(target));
    }

    public static Trio<Integer, String, String> findByFirst(List<Trio<Integer, String, String>> list, Integer target) {
        return list.stream()
                .filter(trio -> trio.getDia().equals(target))
                .findFirst()
                .orElse(null); // Devuelve null si no encuentra ninguno
    }

}
