package be.kdg.sa.warehouse.repository;

import be.kdg.sa.warehouse.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WarehouseRepository  extends JpaRepository<Warehouse, UUID> {

    Warehouse findByMaterialType(String materialType);
}
