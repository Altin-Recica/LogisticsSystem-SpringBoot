package be.kdg.sa.warehouse.service;

import be.kdg.sa.warehouse.domain.Warehouse;
import be.kdg.sa.warehouse.repository.WarehouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Service
public class WarehouseService {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);
    private final WarehouseRepository warehouseRepository;
    private final ConfigurationService configService;

    public WarehouseService(WarehouseRepository warehouseRepository, ConfigurationService configService) {
        this.warehouseRepository = warehouseRepository;
        this.configService = configService;
    }

    public Collection<Warehouse> getWarehouses() {
        logger.info("Retrieving all warehouses");
        try {
            return this.warehouseRepository.findAll();
        } catch (Exception e) {
            logger.error("Failed to retrieve warehouses: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public Warehouse assignWarehouse(String materialType) {
        logger.info("Assigning warehouse for material type {}", materialType);
        try {
            for (Warehouse warehouse : this.getWarehouses()) {
                if (warehouse.getMateriaalsoort().equals(materialType)) {
                    logger.info("Assigned warehouse {}", warehouse.getWarehouseNumber());
                    return warehouse;
                }
            }
            logger.warn("No warehouse found for material type {}", materialType);
            return null;
        } catch (Exception e) {
            logger.error("Error while assigning warehouse for material type {}: {}", materialType, e.getMessage());
            return null;
        }
    }

    public void addInventory(UUID warehouseId, double quantity) {
        logger.info("Adding inventory for warehouse {}", warehouseId);
        try {
            Warehouse warehouse = warehouseRepository.findById(warehouseId)
                    .orElseThrow(() -> new IllegalArgumentException("Warehouse not found with ID: " + warehouseId));

            double potentialFilling = warehouse.getCurrentCapacity() + quantity;

            if (potentialFilling <= configService.getMaxCapacity()) {
                warehouse.setCurrentCapacity(potentialFilling);
            } else if (potentialFilling <= configService.getOverflowCapacity()) {
                logger.warn("Warehouse {} has crossed the overflow capacity", warehouse.getWarehouseNumber());
                warehouse.setCurrentCapacity(potentialFilling);
            } else {
                logger.error("Warehouse {} is full. Cannot add more inventory.", warehouse.getWarehouseNumber());
                return;
            }

            warehouseRepository.save(warehouse);
            logger.info("Inventory added to warehouse {}", warehouse.getWarehouseNumber());
        } catch (IllegalArgumentException e) {
            logger.error("Failed to add inventory: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while adding inventory to warehouse {}: {}", warehouseId, e.getMessage());
        }
    }

    public void removeInventory(UUID warehouseId, double quantity) {
        logger.info("Removing inventory for warehouse {}", warehouseId);
        try {
            Warehouse warehouse = warehouseRepository.findById(warehouseId)
                    .orElseThrow(() -> new IllegalArgumentException("Warehouse not found with ID: " + warehouseId));

            double remainingFilling = warehouse.getCurrentCapacity() - quantity;

            if (remainingFilling >= 0) {
                warehouse.setCurrentCapacity(remainingFilling);
            } else {
                logger.error("Insufficient inventory to remove from warehouse {}. Requested: {}, Available: {}",
                        warehouse.getWarehouseNumber(), quantity, warehouse.getCurrentCapacity());
                throw new IllegalArgumentException("Insufficient inventory to remove");
            }

            warehouseRepository.save(warehouse);
            logger.info("Inventory removed from warehouse {}", warehouse.getWarehouseNumber());
        } catch (IllegalArgumentException e) {
            logger.error("Failed to remove inventory: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while removing inventory from warehouse {}: {}", warehouseId, e.getMessage());
        }
    }

    public boolean checkCapacity(String materialType) {
        Warehouse warehouse = warehouseRepository.findByMaterialType(materialType);

        double currentFilling = warehouse.getCurrentCapacity();

        logger.info("Checking capacity for material type {}: current filling {}", materialType, currentFilling);
        return currentFilling < (configService.getOverflowCapacity() * 0.8);
    }
}
