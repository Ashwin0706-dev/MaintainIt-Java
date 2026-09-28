package com.example.MaintainIt.controller;

import com.example.MaintainIt.dto.TechnicianRequest;
import com.example.MaintainIt.model.Technician;
import com.example.MaintainIt.service.TechnicianService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technicians")
public class TechnicianController {

    private final TechnicianService technicianService;

    public TechnicianController(
            TechnicianService technicianService) {

        this.technicianService =
                technicianService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Technician createTechnician(
            @Valid @RequestBody TechnicianRequest request) {

        return technicianService.createTechnician(
                request
        );
    }


    @GetMapping
    public List<Technician> getAllTechnicians() {

        return technicianService
                .getAllTechnicians();
    }
}