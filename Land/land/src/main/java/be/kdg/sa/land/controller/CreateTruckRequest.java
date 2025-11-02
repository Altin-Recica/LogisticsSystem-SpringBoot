package be.kdg.sa.land.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateTruckRequest(
        @NotBlank String licencePlate,
        @Positive double capacity,
        @Positive double currentWeight,
        @Positive double tareWeight,
        LocalDateTime arrivalTime,
        boolean hasAppointment
) { }

