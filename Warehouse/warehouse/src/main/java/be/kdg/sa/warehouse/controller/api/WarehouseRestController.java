package be.kdg.sa.warehouse.controller.api;

import be.kdg.sa.warehouse.service.WarehouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WarehouseRestController {
    private final WarehouseService warehouseService;
    private static final Logger logger = LoggerFactory.getLogger(WarehouseRestController.class);

    public WarehouseRestController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/check-capacity/{materialType}")
    public ResponseEntity<String> checkCapacity(@PathVariable String materialType) {
        try {
            boolean capacityOk = warehouseService.checkCapacity(materialType);
            return ResponseEntity.ok(capacityOk ? "OK" : "NOK");
        } catch (Exception e) {
            logger.warn("Failed to check capacity for material type {}: {}", materialType, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error checking capacity");
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        logger.warn("An unexpected error occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
