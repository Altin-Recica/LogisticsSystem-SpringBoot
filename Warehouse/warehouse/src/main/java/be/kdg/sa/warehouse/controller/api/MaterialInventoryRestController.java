package be.kdg.sa.warehouse.controller.api;

import be.kdg.sa.warehouse.controller.dto.MaterialInventoryDto;
import be.kdg.sa.warehouse.domain.MaterialInventory;
import be.kdg.sa.warehouse.service.MaterialInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MaterialInventoryRestController {
    private final MaterialInventoryService materialInventoryService;
    private static final Logger logger = LoggerFactory.getLogger(MaterialInventoryRestController.class);

    public MaterialInventoryRestController(MaterialInventoryService materialInventoryService) {
        this.materialInventoryService = materialInventoryService;
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<MaterialInventoryDto>> getMaterialsInInventory() {
        try {
            List<MaterialInventoryDto> materialInventoryDtos = this.materialInventoryService.getMaterialsInInventory()
                    .stream()
                    .map(this::mapToDto)
                    .toList();
            return ResponseEntity.ok(materialInventoryDtos);
        } catch (Exception e) {
            logger.warn("Failed to retrieve materials in inventory: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/inventory/oldestFirst")
    public ResponseEntity<List<MaterialInventoryDto>> getMaterialsInventoryOldestFirst() {
        try {
            List<MaterialInventoryDto> materialInventoryDtos = this.materialInventoryService.getOldestMaterialInventories()
                    .stream()
                    .map(this::mapToDto)
                    .toList();
            return ResponseEntity.ok(materialInventoryDtos);
        } catch (Exception e) {
            logger.warn("Failed to retrieve materials in inventory sorted by oldest first: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/inventory/{warehouseNumber}")
    public ResponseEntity<List<MaterialInventoryDto>> getMaterialsInventoryByWarehouse(@PathVariable int warehouseNumber) {
        try {
            List<MaterialInventoryDto> materialInventoryDtos = this.materialInventoryService.getMaterialInventoriesForWarehouse(warehouseNumber)
                    .stream()
                    .map(this::mapToDto)
                    .toList();
            return ResponseEntity.ok(materialInventoryDtos);
        } catch (Exception e) {
            logger.warn("Failed to retrieve materials in inventory for warehouse {}: {}", warehouseNumber, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private MaterialInventoryDto mapToDto(MaterialInventory materialInventory) {
        return new MaterialInventoryDto(
                materialInventory.getMaterialInventoryId(),
                materialInventory.getQuantity(),
                materialInventory.getArrivalDate(),
                materialInventory.getMaterial(),
                materialInventory.getWarehouse()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        logger.warn("An unexpected error occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}