package be.kdg.sa.warehouse.controller.api;

import be.kdg.sa.warehouse.controller.dto.MaterialDto;
import be.kdg.sa.warehouse.domain.Material;
import be.kdg.sa.warehouse.service.ConfigurationService;
import be.kdg.sa.warehouse.service.MaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialRestController {
    private final MaterialService materialService;
    private final ConfigurationService configService;
    private static final Logger logger = LoggerFactory.getLogger(MaterialRestController.class);

    public MaterialRestController(MaterialService materialService, ConfigurationService configService) {
        this.configService = configService;
        this.materialService = materialService;
    }

    @GetMapping
    public ResponseEntity<List<MaterialDto>> getAllMaterials() {
        try {
            List<MaterialDto> materials = this.materialService.getMaterials()
                    .stream()
                    .map(this::mapToDto)
                    .toList();
            return ResponseEntity.ok(materials);
        } catch (Exception e) {
            logger.warn("Failed to retrieve materials: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<MaterialDto> getMaterial(@PathVariable String name) {
        try {
            return this.materialService.getMaterial(name)
                    .map(material -> ResponseEntity.ok(mapToDto(material)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.warn("Failed to retrieve material with name {}: {}", name, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private MaterialDto mapToDto(Material material) {
        return new MaterialDto(
                material.getMaterialId(),
                material.getName(),
                material.getDescription(),
                configService.getStorageCost(material.getName()),
                configService.getSellingPrice(material.getName())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        logger.warn("An unexpected error occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}