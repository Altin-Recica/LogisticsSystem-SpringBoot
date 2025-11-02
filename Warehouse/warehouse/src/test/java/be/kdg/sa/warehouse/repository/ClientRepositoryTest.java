package be.kdg.sa.warehouse.repository;

import be.kdg.sa.warehouse.domain.Client;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void testSaveClient() {
        Client client = new Client();
        client.setClientID(1L);
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");

        Client savedClient = clientRepository.save(client);

        assertNotNull(savedClient);
        assertEquals(1L, savedClient.getClientID());
        assertEquals("John Doe", savedClient.getName());
        assertEquals("john.doe@example.com", savedClient.getEmail());
    }

    @Test
    public void testFindById() {
        Client client = new Client();
        client.setClientID(2L);
        client.setName("Jane Doe");
        client.setEmail("jane.doe@example.com");
        clientRepository.save(client);

        Optional<Client> foundClient = clientRepository.findById(2L);

        assertTrue(foundClient.isPresent());
        assertEquals("Jane Doe", foundClient.get().getName());
    }

    @Test
    public void testUpdateClient() {
        Client client = new Client();
        client.setClientID(3L);
        client.setName("Alice Smith");
        client.setEmail("alice.smith@example.com");
        clientRepository.save(client);

        client.setName("Alice Johnson");
        Client updatedClient = clientRepository.save(client);

        assertEquals("Alice Johnson", updatedClient.getName());
    }

    @Test
    public void testDeleteClient() {
        Client client = new Client();
        client.setClientID(4L);
        client.setName("Bob Brown");
        client.setEmail("bob.brown@example.com");
        clientRepository.save(client);

        clientRepository.deleteById(4L);
        Optional<Client> deletedClient = clientRepository.findById(4L);

        assertFalse(deletedClient.isPresent());
    }

    @Test
    public void testFindAllClients() {
        Client client1 = new Client();
        client1.setClientID(5L);
        client1.setName("Tom White");
        client1.setEmail("tom.white@example.com");

        Client client2 = new Client();
        client2.setClientID(6L);
        client2.setName("Lisa Green");
        client2.setEmail("lisa.green@example.com");

        clientRepository.save(client1);
        clientRepository.save(client2);

        List<Client> clients = clientRepository.findAll();

        assertTrue(clients.size() >= 2);
    }
}