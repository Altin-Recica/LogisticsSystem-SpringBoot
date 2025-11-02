package be.kdg.sa.land.repository;

import be.kdg.sa.land.domain.FIFOQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface FIFOQueueRepository extends JpaRepository<FIFOQueue, UUID> {
    Optional<FIFOQueue> findFirstByOrderByQueueIDAsc();
}
