package be.kdg.sa.land.controller.dto;

import java.util.UUID;

public record MaterialDTO(
        UUID materialId,
        String name,
        String description,
        double storagePrice,
        double sellingPrice
) { }
