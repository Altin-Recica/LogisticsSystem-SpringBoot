package be.kdg.sa.warehouse.service;

import be.kdg.sa.warehouse.domain.Warehouse;
import be.kdg.sa.warehouse.repository.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class WarehouseServiceTest {
    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Test
    public void testGetWarehouses() {
        // Setup test data
        Warehouse warehouse1 = new Warehouse();
        Warehouse warehouse2 = new Warehouse();
        warehouseRepository.saveAll(Arrays.asList(warehouse1, warehouse2));

        // Execute service call
        Collection<Warehouse> warehouses = warehouseService.getWarehouses();

        // Verify the result
        assertEquals(2, warehouses.size() - 7); //-7 omdat er al 7 warehouses zijn in de databank
    }

    @Test
    public void testAssignWarehouse() {
        // Setup test data
        Warehouse warehouse = new Warehouse();
        warehouse.setMaterialType("Steel");
        warehouseRepository.save(warehouse);

        // Execute service call
        Warehouse result = warehouseService.assignWarehouse("Steel");

        // Verify the result
        assertNotNull(result);
        assertEquals("Steel", result.getMateriaalsoort());
    }

    @Test
    public void testAddInventory() {
        // Setup test data
        Warehouse warehouse = new Warehouse();
        warehouse.setCurrentCapacity(50.0);
        warehouseRepository.save(warehouse);

        UUID warehouseId = warehouse.getWarehouseID();

        // Execute service call
        warehouseService.addInventory(warehouseId, 25.0);

        // Verify the result
        Warehouse updatedWarehouse = warehouseRepository.findById(warehouseId).orElseThrow();
        assertEquals(75.0, updatedWarehouse.getCurrentCapacity());
    }

    @Test
    public void testRemoveInventory() {
        // Setup test data
        Warehouse warehouse = new Warehouse();
        warehouse.setCurrentCapacity(100.0);
        warehouseRepository.save(warehouse);

        UUID warehouseId = warehouse.getWarehouseID();

        // Execute service call
        warehouseService.removeInventory(warehouseId, 25.0);

        // Verify the result
        Warehouse updatedWarehouse = warehouseRepository.findById(warehouseId).orElseThrow();
        assertEquals(75.0, updatedWarehouse.getCurrentCapacity());
    }
}