package be.kdg.sa.warehouse.controller.dto;

import be.kdg.sa.warehouse.domain.Material;
import be.kdg.sa.warehouse.domain.Warehouse;
import java.time.LocalDateTime;
import java.util.UUID;

public record MaterialInventoryDto(UUID materialInventoryId, double quantity, LocalDateTime arrivalDate, Material material, Warehouse warehouse) {
}
