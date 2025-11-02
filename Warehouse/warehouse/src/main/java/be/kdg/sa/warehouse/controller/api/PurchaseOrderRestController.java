package be.kdg.sa.warehouse.controller.api;

import be.kdg.sa.warehouse.controller.dto.PurchaseOrderDto;
import be.kdg.sa.warehouse.domain.PurchaseOrder;
import be.kdg.sa.warehouse.service.PurchaseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PurchaseOrderRestController {
    private final PurchaseOrderService service;
    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderRestController.class);

    public PurchaseOrderRestController(PurchaseOrderService service) {
        this.service = service;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<PurchaseOrderDto>> getAllOrders() {
        try {
            List<PurchaseOrderDto> orders = service.getAllPurchaseOrders().stream()
                    .map(this::mapToDto)
                    .toList();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.warn("Failed to retrieve all orders: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/fulfill-order/{orderId}")
    public ResponseEntity<PurchaseOrder> fulfillPO(@PathVariable Long orderId) {
        try {
            PurchaseOrder po = service.fulfillOrder(orderId);
            return ResponseEntity.ok(po);
        } catch (Exception e) {
            logger.warn("Failed to fulfill order with ID {}: {}", orderId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/orders/create")
    @PreAuthorize("hasAuthority('manager')")
    public ResponseEntity<Optional<PurchaseOrder>> createPO(@RequestBody PurchaseOrder purchaseOrder) {
        try {
            Optional<PurchaseOrder> createdPO = service.createPurchaseOrder(purchaseOrder.getClientNumber(), purchaseOrder.getClientName());
            return new ResponseEntity<>(createdPO, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.warn("Failed to create purchase order: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/purchase-orders")
    @PreAuthorize("hasAuthority('manager')")
    public ResponseEntity<String> createPurchaseOrders(@RequestBody List<PurchaseOrder> purchaseOrders) {
        try {
            List<PurchaseOrder> purchaseOrderList = purchaseOrders.stream()
                    .map(service::createPurchaseOrderJson)
                    .toList();
            return ResponseEntity.ok("Created " + purchaseOrderList.size() + " purchase orders\n");
        } catch (Exception e) {
            logger.warn("Failed to create multiple purchase orders: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create purchase orders");
        }
    }

    private PurchaseOrderDto mapToDto(PurchaseOrder purchaseOrder) {
        return new PurchaseOrderDto(
                purchaseOrder.getOrderId(),
                purchaseOrder.getOrderDate(),
                purchaseOrder.getClientNumber(),
                purchaseOrder.getClientName(),
                purchaseOrder.getTotalAmount(),
                purchaseOrder.isFulfilled(),
                purchaseOrder.getOrderLines()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        logger.warn("An unexpected error occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}