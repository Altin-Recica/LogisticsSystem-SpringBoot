package be.kdg.sa.land.repository;

import be.kdg.sa.land.domain.Terrain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TerrainRepository extends JpaRepository<Terrain, UUID> {
    Optional<Terrain> findFirstByOrderByTerrainID();
}

