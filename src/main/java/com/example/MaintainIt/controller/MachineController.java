package com.example.MaintainIt.controller;

import com.example.MaintainIt.dto.MachineRequest;
import com.example.MaintainIt.model.Machine;
import com.example.MaintainIt.service.MachineService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machines")
public class MachineController {

    private final MachineService machineService;

    public MachineController(
            MachineService machineService) {

        this.machineService = machineService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Machine createMachine(
            @Valid @RequestBody MachineRequest request) {

        return machineService.createMachine(
                request
        );
    }


    @GetMapping
    public List<Machine> getAllMachines() {

        return machineService.getAllMachines();
    }


    @GetMapping("/{id}")
    public Machine getMachine(
            @PathVariable Long id) {

        return machineService.getMachine(id);
    }
}