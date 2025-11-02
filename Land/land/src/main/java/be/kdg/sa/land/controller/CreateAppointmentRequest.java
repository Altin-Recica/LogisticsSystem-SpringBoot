package be.kdg.sa.land.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        @NotNull LocalDateTime plannedTime,

        @Pattern(regexp = "\\d-[A-Za-z]{3}-\\d{3}", message = "License plate must follow the format (1-ABC-123)")
        @NotBlank String licencePlate,

        @NotBlank String material,
        @Positive long clientId
) { }

