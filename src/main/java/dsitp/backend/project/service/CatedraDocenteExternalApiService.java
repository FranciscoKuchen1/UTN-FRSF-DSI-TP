package dsitp.backend.project.service;

import dsitp.backend.project.integration.CatedraResponse;
import dsitp.backend.project.integration.DocenteResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
public class CatedraDocenteExternalApiService {

    private final WebClient webClient;

    @Autowired
    public CatedraDocenteExternalApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://catedradocenteexternalclient.com/api")
                .build();
    }

    public List<CatedraResponse> getHardcodedCatedras() {
        return List.of(
                new CatedraResponse(1, "Análisis Numérico"),
                new CatedraResponse(2, "Física II"),
                new CatedraResponse(3, "Probabilidad y Estadística"));
    }

    public List<DocenteResponse> getHardcodedDocentes() {
        return List.of(
                new DocenteResponse(1, "Juan", "Pérez", "jperez@gmail.com"),
                new DocenteResponse(2, "María", "Gómez", "gomezmaria88@outlook.com"),
                new DocenteResponse(3, "Carlos", "López", "carloslopezk@hotmail.com"));
    }

    public CatedraResponse getCatedraById(Integer idCatedra) {
        return getHardcodedCatedras().stream()
                .filter(catedra -> catedra.getId().equals(idCatedra))
                .findFirst()
                .orElse(null);

        // try {
        // return webClient.get()
        // .uri("/catedras/{id}", idCatedra)
        // .retrieve()
        // .bodyToMono(CatedraResponse.class)
        // .block();
        // } catch (Exception ex) {
        // throw new RuntimeException("No se pudo obtener la catedra", ex);
        // }
    }

    public DocenteResponse getDocenteById(Integer idDocente) {
        return getHardcodedDocentes().stream()
                .filter(docente -> docente.getId().equals(idDocente))
                .findFirst()
                .orElse(null);
        // try {
        // return webClient.get()
        // .uri("/docentes/{id}", idDocente)
        // .retrieve()
        // .bodyToMono(DocenteResponse.class)
        // .block();
        // } catch (Exception ex) {
        // throw new RuntimeException("No se pudo obtener el docente", ex);
        // }
    }
}
