package be.kdg.sa.warehouse.repository;

import be.kdg.sa.warehouse.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MaterialRepository extends JpaRepository<Material, UUID> {

        Optional<Material> findByName(String name);
}
