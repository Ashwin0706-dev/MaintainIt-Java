package com.example.MaintainIt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CompleteTaskRequest(

        @NotNull(message = "Technician ID is required")
        @Positive(message = "Technician ID must be greater than 0")
        Long technicianId,

        @NotBlank(message = "Completion notes are required")
        String notes

) {
}