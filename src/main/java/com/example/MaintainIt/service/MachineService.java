package com.example.MaintainIt.service;

import com.example.MaintainIt.dto.MachineRequest;
import com.example.MaintainIt.exception.BusinessRuleException;
import com.example.MaintainIt.exception.ResourceNotFoundException;
import com.example.MaintainIt.model.Machine;
import com.example.MaintainIt.repository.MachineRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MachineService {

    private final MachineRepository machineRepository;

    public MachineService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }


    @Transactional
    public Machine createMachine(MachineRequest request) {

        String machineCode =
                request.machineCode().trim();

        if (machineRepository
                .existsByMachineCodeIgnoreCase(machineCode)) {

            throw new BusinessRuleException(
                    "A machine with code '"
                            + machineCode
                            + "' already exists."
            );
        }

        Machine machine = new Machine();

        machine.setMachineName(
                request.machineName().trim()
        );

        machine.setMachineCode(machineCode);

        machine.setIntervalType(
                request.intervalType()
        );

        machine.setMaintenanceInterval(
                request.maintenanceInterval()
        );

        machine.setCurrentUsageHours(0.0);

        machine.setUsageAtLastMaintenance(0.0);

        machine.setLastMaintenanceDate(
                LocalDateTime.now()
        );

        machine.setActive(true);

        return machineRepository.save(machine);
    }


    public List<Machine> getAllMachines() {

        return machineRepository.findAll();
    }


    public Machine getMachine(Long id) {

        return machineRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Machine with ID "
                                        + id
                                        + " not found."
                        )
                );
    }


    public List<Machine> getActiveMachines() {

        return machineRepository.findByActiveTrue();
    }
}