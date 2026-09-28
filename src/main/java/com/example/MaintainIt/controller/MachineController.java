package com.example.MaintainIt.controller;

import com.example.MaintainIt.dto.MachineRequest;
import com.example.MaintainIt.model.Machine;
import com.example.MaintainIt.service.MachineService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machines")
@CrossOrigin
public class MachineController {

    private final MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    // CREATE MACHINE
    @PostMapping
    public ResponseEntity<Machine> createMachine(
            @Valid @RequestBody MachineRequest request) {

        return ResponseEntity.ok(
                machineService.createMachine(request)
        );
    }

    // GET ALL MACHINES
    @GetMapping
    public ResponseEntity<List<Machine>> getAllMachines() {

        return ResponseEntity.ok(
                machineService.getAllMachines()
        );
    }

    // DELETE MACHINE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMachine(
            @PathVariable Long id) {

        machineService.deleteMachine(id);

        return ResponseEntity.ok(
                "Machine deleted successfully"
        );
    }
}