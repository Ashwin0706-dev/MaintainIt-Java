package com.example.MaintainIt;

import com.example.MaintainIt.controller.TechnicianController;
import com.example.MaintainIt.exception.GlobalExceptionHandler;
import com.example.MaintainIt.service.TechnicianService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TechnicianControllerValidationTest {

    private MockMvc mockMvc;

    @Mock
    private TechnicianService technicianService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TechnicianController(technicianService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testInvalidPhoneNumbersReturn400() throws Exception {
        String[] invalidPhones = {
                "987654321",
                "987654321012",
                "-9876543210",
                "98765-43210",
                "98765 43210",
                "abcdefghij",
                "98765abc10",
                "98765@3210"
        };

        for (String phone : invalidPhones) {
            String json = """
                    {
                        "name": "John Doe",
                        "email": "john@example.com",
                        "phone": "%s"
                    }
                    """.formatted(phone);

            mockMvc.perform(post("/api/technicians")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Validation failed"))
                    .andExpect(jsonPath("$.message", containsString("phone: Phone number must contain exactly 10 digits")));
        }
    }
}
