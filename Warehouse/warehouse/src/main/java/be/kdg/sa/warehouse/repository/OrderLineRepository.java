package be.kdg.sa.warehouse.repository;

import be.kdg.sa.warehouse.domain.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
}
