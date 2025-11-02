package be.kdg.sa.warehouse.repository;

import be.kdg.sa.warehouse.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
