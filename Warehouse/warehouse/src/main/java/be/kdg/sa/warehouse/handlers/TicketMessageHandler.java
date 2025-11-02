package be.kdg.sa.warehouse.handlers;

import be.kdg.sa.warehouse.domain.Material;
import be.kdg.sa.warehouse.domain.Warehouse;
import be.kdg.sa.warehouse.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Map;

import static be.kdg.sa.warehouse.config.RabbitTopology.TOPIC_QUEUE_TICKET;

@Component
public class TicketMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(TicketMessageHandler.class);
    private final ObjectMapper objectMapper;
    private final WarehouseService warehouseService;
    private final MaterialInventoryService materialInventoryService;
    private final MaterialService materialService;

    public TicketMessageHandler(ObjectMapper objectMapper, WarehouseService warehouseService, MaterialInventoryService materialInventoryService, MaterialService materialService) {
        this.objectMapper = objectMapper;
        this.warehouseService = warehouseService;
        this.materialInventoryService = materialInventoryService;
        this.materialService = materialService;
    }

    @RabbitListener(queues = TOPIC_QUEUE_TICKET)
    public void receiveMessage(String message) throws JsonProcessingException {
        Map<String, Object> ticketData = objectMapper.readValue(message, new TypeReference<>() {});
        logger.info("Received message: {}", message);

        handleReceivedTicket(ticketData);
    }

    private void handleReceivedTicket(Map<String, Object> ticketData) {
        Integer clientId = (Integer) ticketData.get("clientId");
        String type = (String) ticketData.get("type");
        Double weight = (Double) ticketData.get("weight");
        String timestamp = (String) ticketData.get("timestamp");

        logger.info("Client ID: {}", clientId);
        logger.info("Material Type: {}", type);
        logger.info("Weight: {}", weight);
        logger.info("Timestamp: {}", timestamp);

        if (materialService.getMaterial(type).isPresent()) {
            Material material = materialService.getMaterial(type).get();
            assingValues(type, material, clientId, weight, timestamp);
        } else {
            logger.info("Material not found: {}", type);
        }
    }

    public void assingValues(String materialtype, Material material, int clientId, double weight, String timestamp) {
        Warehouse assignedWarehouse = warehouseService.assignWarehouse(materialtype);
        logger.info("Truck assigned to Warehouse: {}", assignedWarehouse.getNumber());
        materialInventoryService.addMaterialInventory(assignedWarehouse, material, clientId, weight, LocalDateTime.parse(timestamp));
        logger.info("Material {} added to Inventory", material.getName());
        warehouseService.addInventory(assignedWarehouse.getWarehouseID(), weight);
        logger.info("Inventory added to Warehouse: {}", assignedWarehouse.getNumber());
    }
}