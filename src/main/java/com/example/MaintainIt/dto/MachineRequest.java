package com.example.MaintainIt.dto;

import com.example.MaintainIt.model.MaintenanceIntervalType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MachineRequest(

        @NotBlank(message = "Machine name is required")
        @Size(
                max = 100,
                message = "Machine name cannot exceed 100 characters"
        )
        @Pattern(
                regexp = "^[A-Za-z ]+$",
                message = "Machine name must contain letters and spaces only"
        )
        String machineName,


        @NotBlank(message = "Machine code is required")
        @Size(
                max = 20,
                message = "Machine code cannot exceed 20 characters"
        )
        @Pattern(
                regexp = "^[A-Za-z][0-9]+$",
                message = "Machine code must contain one letter followed by numbers"
        )
        String machineCode,


        @NotNull(message = "Maintenance interval type is required")
        MaintenanceIntervalType intervalType,


        @NotNull(message = "Maintenance interval is required")
        @Positive(
                message = "Maintenance interval must be greater than 0"
        )
        Integer maintenanceInterval

) {
}