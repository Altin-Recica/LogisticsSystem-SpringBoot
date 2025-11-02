package be.kdg.sa.warehouse.repository;

import be.kdg.sa.warehouse.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByClientNumber(double number);
}
