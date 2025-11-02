package be.kdg.sa.warehouse.service;

import be.kdg.sa.warehouse.domain.Material;
import be.kdg.sa.warehouse.repository.MaterialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MaterialService {
    private static final Logger logger = LoggerFactory.getLogger(MaterialService.class);
    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public Optional<Material> getMaterial(String name) {
        logger.info("Getting material: {}", name);
        try {
            return this.materialRepository.findByName(name);
        } catch (Exception e) {
            logger.error("Failed to retrieve material '{}': {}", name, e.getMessage());
            return Optional.empty();
        }
    }

    public Collection<Material> getMaterials() {
        logger.info("Getting all materials");
        try {
            return this.materialRepository.findAll();
        } catch (Exception e) {
            logger.error("Failed to retrieve all materials: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public Material addMaterial(Material material) {
        logger.info("Creating material: {}", material);
        try {
            return this.materialRepository.save(material);
        } catch (Exception e) {
            logger.error("Failed to create material '{}': {}", material.getName(), e.getMessage());
            return null;
        }
    }
}
