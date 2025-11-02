package be.kdg.sa.warehouse.controller.api;

import be.kdg.sa.warehouse.controller.dto.ClientDto;
import be.kdg.sa.warehouse.domain.Client;
import be.kdg.sa.warehouse.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientRestController {
    private final ClientService clientService;
    private static final Logger logger = LoggerFactory.getLogger(ClientRestController.class);


    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        try {
            List<ClientDto> clients = this.clientService.getClients()
                    .stream()
                    .map(this::mapToDto)
                    .toList();
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            logger.warn("Failed to retrieve clients: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ClientDto mapToDto(Client client) {
        return new ClientDto(client.getClientID(), client.getName(), client.getEmail());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        logger.warn("An unexpected error occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
