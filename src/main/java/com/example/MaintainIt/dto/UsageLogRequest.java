package com.example.MaintainIt.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UsageLogRequest(

        @NotNull(message = "Machine ID is required")
        @Positive(message = "Machine ID must be greater than 0")
        Long machineId,

        @NotNull(message = "Hours used is required")
        @Positive(message = "Hours used must be greater than 0")
        Double hoursUsed

) {
}