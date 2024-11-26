package dsitp.backend.project.service;

import dsitp.backend.project.integration.PasswordValidationRequest;
import dsitp.backend.project.integration.PasswordValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
public class ExternalApiService {

    private final WebClient webClient;

    @Autowired
    public ExternalApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://sistema-externo.com/api").build();
    }

    public boolean validatePassword(String contrasena) {
        try {
            PasswordValidationRequest request = new PasswordValidationRequest(contrasena);
            PasswordValidationResponse response = webClient.post()
                    .uri("/validate-password")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(PasswordValidationResponse.class)
                    .block();

            return response != null && response.isValid();
        } catch (WebClientException e) {

            return false;
        }
    }
}
