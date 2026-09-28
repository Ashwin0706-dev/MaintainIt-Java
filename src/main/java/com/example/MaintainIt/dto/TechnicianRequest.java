package com.example.MaintainIt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TechnicianRequest(

        @NotBlank(message = "Technician name is required")
        @Size(
                min = 2,
                max = 100,
                message = "Technician name must be between 2 and 100 characters"
        )
        @Pattern(
                regexp = "^[A-Za-z][A-Za-z .'-]*$",
                message = "Technician name must contain letters only " +
                        "(spaces, apostrophes, periods and hyphens are allowed) " +
                        "and cannot contain numbers or be blank"
        )
        String name,

        @NotBlank(message = "Technician email is required")
        @Size(
                max = 254,
                message = "Email cannot exceed 254 characters"
        )
        @Email(
                message = "Enter a valid email address (e.g. name@example.com)"
        )
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$",
                message = "Enter a valid email address (e.g. name@example.com)"
        )
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^[0-9]{10}$",
                message = "Phone number must contain exactly 10 digits"
        )
        String phone

) {
}
