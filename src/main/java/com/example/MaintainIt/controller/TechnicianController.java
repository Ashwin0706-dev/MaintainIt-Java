package com.example.MaintainIt.controller;

import com.example.MaintainIt.dto.TechnicianRequest;
import com.example.MaintainIt.model.Technician;
import com.example.MaintainIt.service.TechnicianService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technicians")
@CrossOrigin
public class TechnicianController {

    private final TechnicianService technicianService;

    public TechnicianController(
            TechnicianService technicianService) {

        this.technicianService = technicianService;
    }

    // CREATE TECHNICIAN
    @PostMapping
    public ResponseEntity<Technician> createTechnician(
            @Valid @RequestBody TechnicianRequest request) {

        return ResponseEntity.ok(
                technicianService.createTechnician(request)
        );
    }

    // GET ALL TECHNICIANS
    @GetMapping
    public ResponseEntity<List<Technician>> getAllTechnicians() {

        return ResponseEntity.ok(
                technicianService.getAllTechnicians()
        );
    }

    // DELETE TECHNICIAN
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTechnician(
            @PathVariable Long id) {

        technicianService.deleteTechnician(id);

        return ResponseEntity.ok(
                "Technician deleted successfully"
        );
    }
}