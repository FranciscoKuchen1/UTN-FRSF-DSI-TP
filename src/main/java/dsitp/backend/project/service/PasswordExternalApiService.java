package dsitp.backend.project.service;

import dsitp.backend.project.integration.PasswordValidationRequest;
import dsitp.backend.project.integration.PasswordValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
public class PasswordExternalApiService {

    private final WebClient webClient;

    @Autowired
    public PasswordExternalApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://passwordexternalclient.com/api").build();
    }

    public Boolean validatePassword(String contrasena) {
        try {
            PasswordValidationRequest request = new PasswordValidationRequest(contrasena);
            PasswordValidationResponse response = webClient.post()
                    .uri("/validate-password")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(PasswordValidationResponse.class)
                    .block();

            return response != null && response.isValid();
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo obtener el docente", ex);
        }
    }

}
