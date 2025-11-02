package be.kdg.sa.warehouse.controller.dto;
import java.util.UUID;

public record MaterialDto(
        UUID materialId,
        String name,
        String description,
        double storagePrice,
        double sellingPrice
){}
