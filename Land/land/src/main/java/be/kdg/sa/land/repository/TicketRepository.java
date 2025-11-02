package be.kdg.sa.land.repository;

import be.kdg.sa.land.domain.WeighbridgeTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<WeighbridgeTicket, UUID> {
}
