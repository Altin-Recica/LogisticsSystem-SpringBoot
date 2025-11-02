package be.kdg.sa.land.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateTicketRequest(
        @NotBlank String licencePlate,
        @Positive double teraWeight,
        @Positive double grossWeight,
        @NotBlank String type,
        @Positive long clientId
) { }

