package be.kdg.sa.warehouse.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConfigurationServiceUnitTest {
    @Autowired
    private ConfigurationService configurationService;

    @Test
    void testGetMaxCapacity() {
        assertEquals(500000, configurationService.getMaxCapacity());
    }

    @Test
    void testGetOverflowCapacity() {
        assertEquals(550000, configurationService.getOverflowCapacity());
    }

    @Test
    void testGetCommissionRate() {
        assertEquals(0.01, configurationService.getCommissionRate());
    }

    @Test
    void testGetStorageCostForGips() {
        assertEquals(1.0, configurationService.getStorageCost("gips"));
    }

    @Test
    void testGetStorageCostForUnknownMaterial() {
        assertEquals(0.0, configurationService.getStorageCost("unknown"));
    }

    @Test
    void testGetSellingPrices() {
        var sellingPrices = configurationService.getSellingPrices();
        assertEquals(13.0, sellingPrices.get("Gips"));
        assertEquals(110.0, sellingPrices.get("Ijzererts"));
        assertEquals(95.0, sellingPrices.get("Cement"));
        assertEquals(210.0, sellingPrices.get("Petcoke"));
        assertEquals(160.0, sellingPrices.get("Slak"));
    }

    @Test
    void testGetStorageCosts() {
        var storageCosts = configurationService.getStorageCosts();
        assertEquals(1.0, storageCosts.get("Gips"));
        assertEquals(5.0, storageCosts.get("Ijzererts"));
        assertEquals(3.0, storageCosts.get("Cement"));
        assertEquals(10.0, storageCosts.get("Petcoke"));
        assertEquals(7.0, storageCosts.get("Slak"));
    }
}