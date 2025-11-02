package be.kdg.sa.land.service;

import be.kdg.sa.land.domain.Terrain;
import be.kdg.sa.land.domain.Truck;
import be.kdg.sa.land.repository.TerrainRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerrainService {
    private static final Logger logger = LoggerFactory.getLogger(TerrainService.class);
    private final TerrainRepository terrainRepository;

    public TerrainService(TerrainRepository terrainRepository) {
        this.terrainRepository = terrainRepository;
    }

    public void addTruckToTerrain(Truck truck) {
        try {
            Terrain terrain = terrainRepository.findFirstByOrderByTerrainID()
                    .orElseThrow(() -> new EntityNotFoundException("Terrain not found"));
            terrain.addTruck(truck);
            logger.info("Added truck {} to terrain {}", truck.getLicencePlate(), terrain.getTerrainID());
            terrainRepository.save(terrain);
        } catch (EntityNotFoundException e) {
            logger.error("Failed to add truck {} to terrain: {}", truck.getLicencePlate(), e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while adding truck {} to terrain: {}", truck.getLicencePlate(), e.getMessage());
            throw new RuntimeException("Failed to add truck to terrain", e);
        }
    }

    public List<Truck> getAllTrucksInTerrain() {
        try {
            Terrain terrain = terrainRepository.findFirstByOrderByTerrainID()
                    .orElseThrow(() -> new EntityNotFoundException("Terrain not found"));
            logger.info("Found {} trucks in terrain {}", terrain.getTrucks().size(), terrain.getTerrainID());
            return terrain.getTrucks();
        } catch (EntityNotFoundException e) {
            logger.error("Failed to retrieve trucks from terrain: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving trucks from terrain: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve trucks from terrain", e);
        }
    }

}

