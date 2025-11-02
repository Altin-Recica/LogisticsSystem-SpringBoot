package be.kdg.sa.land.controller.api;

import be.kdg.sa.land.controller.dto.ClientDTO;
import be.kdg.sa.land.controller.dto.MaterialDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;

@RestController
public class RestReciever {
    private static final Logger logger = LoggerFactory.getLogger(RestReciever.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public Collection<ClientDTO> getClients() {
        String url = "http://localhost:8080/api/clients";
        try {
            ResponseEntity<Collection<ClientDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return response.getBody();
        } catch (RestClientException e) {
            logger.error("Error fetching clients from {}: {}", url, e.getMessage());
            return Collections.emptyList();
        }
    }

    public Collection<MaterialDTO> getMaterials() {
        String url = "http://localhost:8080/api/materials";
        try {
            ResponseEntity<Collection<MaterialDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return response.getBody();
        } catch (RestClientException e) {
            logger.error("Error fetching materials from {}: {}", url, e.getMessage());
            return Collections.emptyList();
        }
    }

    @GetMapping("/check-capacity/{materialType}")
    public boolean checkCapacity(@PathVariable String materialType) {
        String url = "http://localhost:8080/check-capacity/" + materialType;
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            return "OK".equals(response.getBody());
        } catch (RestClientException e) {
            logger.error("Cannot check capacity: {}", e.getMessage());
            return true;
        }
    }
}
