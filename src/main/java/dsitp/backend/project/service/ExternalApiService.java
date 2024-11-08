package dsitp.backend.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExternalApiService {

    private final WebClient webClient;

    @Autowired
    public ExternalApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getDataFromExternalApi() {

        return webClient.get().uri("/data").retrieve().bodyToMono(String.class).block();
    }
}
