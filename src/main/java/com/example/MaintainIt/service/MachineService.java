package com.example.MaintainIt.service;

import com.example.MaintainIt.dto.MachineRequest;
import com.example.MaintainIt.exception.BusinessRuleException;
import com.example.MaintainIt.exception.ResourceNotFoundException;
import com.example.MaintainIt.model.Machine;
import com.example.MaintainIt.repository.MachineRepository;
import com.example.MaintainIt.repository.MaintenanceTaskRepository;
import com.example.MaintainIt.repository.UsageLogRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MachineService {

    private final MachineRepository machineRepository;
    private final UsageLogRepository usageLogRepository;
    private final MaintenanceTaskRepository maintenanceTaskRepository;

    public MachineService(
            MachineRepository machineRepository,
            UsageLogRepository usageLogRepository,
            MaintenanceTaskRepository maintenanceTaskRepository) {

        this.machineRepository = machineRepository;
        this.usageLogRepository = usageLogRepository;
        this.maintenanceTaskRepository = maintenanceTaskRepository;
    }

    // CREATE MACHINE
    public Machine createMachine(MachineRequest request) {

        if (machineRepository.existsByMachineCode(request.machineCode())) {
            throw new BusinessRuleException(
                    "Machine code already exists: " + request.machineCode()
            );
        }

        Machine machine = new Machine();

        machine.setMachineName(request.machineName().trim());
        machine.setMachineCode(request.machineCode().trim());
        machine.setIntervalType(request.intervalType());
        machine.setMaintenanceInterval(request.maintenanceInterval());

        machine.setCurrentUsageHours(0.0);
        machine.setUsageAtLastMaintenance(0.0);
        machine.setLastMaintenanceDate(LocalDateTime.now());
        machine.setActive(true);

        return machineRepository.save(machine);
    }

    // GET ALL MACHINES
    public List<Machine> getAllMachines() {
        return machineRepository.findAll();
    }

    // DELETE MACHINE
    @Transactional
    public void deleteMachine(Long id) {

        Machine machine = machineRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Machine not found with ID: " + id
                        )
                );

        /*
         * Delete records that depend on this machine first.
         * This prevents foreign-key constraint errors.
         */

        usageLogRepository.deleteAllByMachine(machine);

        maintenanceTaskRepository.deleteAllByMachine(machine);

        // Finally delete the machine
        machineRepository.delete(machine);
    }
}