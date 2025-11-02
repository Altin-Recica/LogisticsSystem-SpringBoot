package be.kdg.sa.warehouse.controller.dto;

public record ClientDto(
        long clientID,
        String name,
        String email
) {}
