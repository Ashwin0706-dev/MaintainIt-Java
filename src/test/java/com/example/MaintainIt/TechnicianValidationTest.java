package com.example.MaintainIt;

import com.example.MaintainIt.dto.TechnicianRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TechnicianValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "9876543210",
            "9123456789",
            "9999999999"
    })
    void validPhoneNumbers(String phone) {
        TechnicianRequest request = new TechnicianRequest("John Doe", "john@example.com", phone);
        Set<ConstraintViolation<TechnicianRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Expected no violations for valid phone: " + phone);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "987654321",        // less than 10 digits
            "987654321012",     // more than 10 digits
            "-9876543210",      // negative
            "98765-43210",      // contains -
            "98765 43210",      // contains space
            "abcdefghij",       // letters
            "98765abc10",       // letters
            "98765@3210"        // special character
    })
    void invalidPhoneNumbers(String phone) {
        TechnicianRequest request = new TechnicianRequest("John Doe", "john@example.com", phone);
        Set<ConstraintViolation<TechnicianRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Expected violations for invalid phone: " + phone);
        boolean hasPhonePatternError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("phone") &&
                        v.getMessage().equals("Phone number must contain exactly 10 digits"));
        assertTrue(hasPhonePatternError, "Expected 'Phone number must contain exactly 10 digits' for: " + phone);
    }

    @Test
    void phoneIsRequired() {
        TechnicianRequest requestNull = new TechnicianRequest("John Doe", "john@example.com", null);
        Set<ConstraintViolation<TechnicianRequest>> violationsNull = validator.validate(requestNull);
        assertFalse(violationsNull.isEmpty(), "Null phone should be rejected");

        TechnicianRequest requestBlank = new TechnicianRequest("John Doe", "john@example.com", "");
        Set<ConstraintViolation<TechnicianRequest>> violationsBlank = validator.validate(requestBlank);
        assertFalse(violationsBlank.isEmpty(), "Blank phone should be rejected");
    }
}
