package be.kdg.sa.warehouse.service;

import be.kdg.sa.warehouse.domain.Client;
import be.kdg.sa.warehouse.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Collection<Client> getClients() {
        logger.info("Getting all clients");
        try {
            return clientRepository.findAll();
        } catch (Exception e) {
            logger.error("Error retrieving clients: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}

