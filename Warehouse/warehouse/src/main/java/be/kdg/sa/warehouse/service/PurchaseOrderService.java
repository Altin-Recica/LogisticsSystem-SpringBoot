package be.kdg.sa.warehouse.service;

import be.kdg.sa.warehouse.domain.Material;
import be.kdg.sa.warehouse.domain.MaterialInventory;
import be.kdg.sa.warehouse.domain.OrderLine;
import be.kdg.sa.warehouse.domain.PurchaseOrder;
import be.kdg.sa.warehouse.repository.MaterialInventoryRepository;
import be.kdg.sa.warehouse.repository.PurchaseOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderService.class);
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final MaterialInventoryRepository materialInventoryRepository;
    private final ConfigurationService configService;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, MaterialInventoryRepository materialInventoryRepository, ConfigurationService configService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.materialInventoryRepository = materialInventoryRepository;
        this.configService = configService;
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        logger.info("Getting all purchase orders");
        try {
            return purchaseOrderRepository.findAll();
        } catch (Exception e) {
            logger.error("Failed to retrieve purchase orders: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public Optional<PurchaseOrder> createPurchaseOrder(long clientNumber, String clientName) {
        logger.info("Creating purchase order for client {} with name {}", clientNumber, clientName);
        double weight = 0;
        try {
            PurchaseOrder newOrder = new PurchaseOrder(clientNumber, clientName, LocalDate.now());
            for (MaterialInventory inventory : materialInventoryRepository.findByClientNumberOrderByArrivalDate(clientNumber)) {
                OrderLine orderLine = new OrderLine(inventory.getMaterial(), inventory.getQuantity(), configService.getSellingPrice(inventory.getMaterial().getName()));
                weight += inventory.getQuantity();
                newOrder.getOrderLines().add(orderLine);
            }

            if (weight > configService.getPurchaseOrderMax()) {
                logger.error("Purchase order for client {} exceeded maximum weight of {}", clientNumber, configService.getPurchaseOrderMax());
                return Optional.empty();
            }

            newOrder.setFulfilled(false);
            return Optional.of(purchaseOrderRepository.save(newOrder));
        } catch (Exception e) {
            logger.error("Failed to create purchase order for client {}: {}", clientNumber, e.getMessage());
            return Optional.empty();
        }
    }

    public PurchaseOrder createPurchaseOrderJson(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder fulfillOrder(Long orderId) {
        logger.info("Fulfilling order {}", orderId);
        try {
            PurchaseOrder order = purchaseOrderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Purchase Order not found with ID: " + orderId));

            if (order.isFulfilled()) {
                logger.warn("Purchase Order {} is already fulfilled. No action taken.", orderId);
                return order;
            }

            for (OrderLine orderLine : order.getOrderLines()) {
                Material material = orderLine.getMaterial();
                double requestedQuantity = orderLine.getQuantity();

                List<MaterialInventory> inventories = materialInventoryRepository.findByMaterial(material);
                double availableQuantity = inventories.stream().mapToDouble(MaterialInventory::getQuantity).sum();

                if (availableQuantity >= requestedQuantity) {
                    allocateMaterialFromInventory(inventories, requestedQuantity);
                } else {
                    throw new IllegalArgumentException("Not enough stock available for material: " + material.getName());
                }
            }

            order.setFulfilled(true);
            order.setTotalAmount(calculateTotalAmount(order.getOrderLines()));
            logger.info("Fulfilled purchase order: {}", order);

            return purchaseOrderRepository.save(order);
        } catch (IllegalArgumentException e) {
            logger.error("Error fulfilling order {}: {}", orderId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error fulfilling order {}: {}", orderId, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<PurchaseOrder> getPurchaseOrdersByClientNumber(long clientNumber) {
        logger.info("Getting purchase orders for client {}", clientNumber);
        try {
            return purchaseOrderRepository.findByClientNumber(clientNumber);
        } catch (Exception e) {
            logger.error("Failed to retrieve purchase orders for client {}: {}", clientNumber, e.getMessage());
            return Collections.emptyList();
        }
    }

    private void allocateMaterialFromInventory(List<MaterialInventory> inventories, double requestedQuantity) {
        logger.info("Allocating material from inventory: {}", inventories);
        try {
            for (MaterialInventory inventory : inventories) {
                if (requestedQuantity <= 0) break;

                if (inventory.getQuantity() >= requestedQuantity) {
                    inventory.setQuantity(inventory.getQuantity() - requestedQuantity);
                    materialInventoryRepository.save(inventory);
                    requestedQuantity = 0;
                } else {
                    requestedQuantity -= inventory.getQuantity();
                    inventory.setQuantity(0);
                    materialInventoryRepository.save(inventory);
                }
            }
            logger.info("Allocated material from inventory: {}", inventories);
        } catch (Exception e) {
            logger.error("Failed to allocate material from inventory: {}", e.getMessage());
        }
    }

    public double calculateTotalAmount(List<OrderLine> orderLines) {
        logger.info("Calculating total amount for order lines: {}", orderLines);
        try {
            return orderLines.stream()
                    .mapToDouble(orderLine -> orderLine.getQuantity() * orderLine.getPricePerTon())
                    .sum();
        } catch (Exception e) {
            logger.error("Failed to calculate total amount: {}", e.getMessage());
            return 0;
        }
    }

    public double calculateCommission(double amount) {
        logger.info("Calculating commission for amount: {}", amount);
        try {
            return amount * configService.getCommissionRate();
        } catch (Exception e) {
            logger.error("Failed to calculate commission: {}", e.getMessage());
            return 0;
        }
    }


}
