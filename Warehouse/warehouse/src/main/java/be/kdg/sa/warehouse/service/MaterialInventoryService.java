package be.kdg.sa.warehouse.service;


import be.kdg.sa.warehouse.domain.Material;
import be.kdg.sa.warehouse.domain.MaterialInventory;
import be.kdg.sa.warehouse.domain.Warehouse;
import be.kdg.sa.warehouse.repository.MaterialInventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class MaterialInventoryService {
    private static final Logger logger = LoggerFactory.getLogger(MaterialInventoryService.class);
    private final MaterialInventoryRepository materialInventoryRepository;

    public MaterialInventoryService(MaterialInventoryRepository materialInventoryRepository) {
        this.materialInventoryRepository = materialInventoryRepository;
    }

    public Collection<MaterialInventory> getMaterialInventoriesForWarehouse(int warehouseNumber) {
        logger.info("Getting material inventories for warehouse {}", warehouseNumber);
        try {
            return materialInventoryRepository.findByWarehouse_WarehouseNumber(warehouseNumber);
        } catch (Exception e) {
            logger.error("Error retrieving material inventories for warehouse {}: {}", warehouseNumber, e.getMessage());
            throw new RuntimeException("Error retrieving material inventories for warehouse " + warehouseNumber, e);
        }
    }

    public List<MaterialInventory> getOldestMaterialInventories() {
        logger.info("Getting oldest material inventories");
        try {
            return materialInventoryRepository.findAllByOrderByArrivalDateAsc();
        } catch (Exception e) {
            logger.error("Error retrieving oldest material inventories: {}", e.getMessage());
            throw new RuntimeException("Error retrieving oldest material inventories", e);
        }
    }

    public List<MaterialInventory> getMaterialsInInventory() {
        logger.info("Getting material inventories");
        try {
            return materialInventoryRepository.findAll();
        } catch (Exception e) {
            logger.error("Error retrieving material inventories: {}", e.getMessage());
            throw new RuntimeException("Error retrieving material inventories", e);
        }
    }

    public void addMaterialInventory(Warehouse warehouse, Material material, int clientNumber, double quantity, LocalDateTime arrivalDate) {
        logger.info("Adding material inventory for client {}, quantity {}", clientNumber, quantity);
        try {
            MaterialInventory materialInventory = new MaterialInventory(material, warehouse, clientNumber, quantity, arrivalDate);
            materialInventoryRepository.save(materialInventory);
        } catch (Exception e) {
            logger.error("Error adding material inventory for client {}: {}", clientNumber, e.getMessage());
            throw new RuntimeException("Error adding material inventory for client " + clientNumber, e);
        }
    }


}