package com.example.MaintainIt.service;

import com.example.MaintainIt.dto.UsageLogRequest;
import com.example.MaintainIt.exception.ResourceNotFoundException;
import com.example.MaintainIt.model.Machine;
import com.example.MaintainIt.model.UsageLog;
import com.example.MaintainIt.repository.MachineRepository;
import com.example.MaintainIt.repository.UsageLogRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsageLogService {

    private final UsageLogRepository usageLogRepository;
    private final MachineRepository machineRepository;
    private final MaintenanceTaskService maintenanceTaskService;

    public UsageLogService(
            UsageLogRepository usageLogRepository,
            MachineRepository machineRepository,
            MaintenanceTaskService maintenanceTaskService) {

        this.usageLogRepository =
                usageLogRepository;

        this.machineRepository =
                machineRepository;

        this.maintenanceTaskService =
                maintenanceTaskService;
    }


    @Transactional
    public UsageLog logUsage(
            UsageLogRequest request) {

        Machine machine =
                machineRepository
                        .findById(request.machineId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Machine with ID "
                                                + request.machineId()
                                                + " not found."
                                )
                        );


        double newUsage =
                machine.getCurrentUsageHours()
                        + request.hoursUsed();


        machine.setCurrentUsageHours(
                newUsage
        );

        machineRepository.save(machine);


        UsageLog usageLog =
                new UsageLog();

        usageLog.setMachine(machine);

        usageLog.setHoursUsed(
                request.hoursUsed()
        );

        usageLog.setCumulativeUsageHours(
                newUsage
        );

        usageLog.setLoggedAt(
                LocalDateTime.now()
        );


        UsageLog savedLog =
                usageLogRepository.save(
                        usageLog
                );


        /*
         * Immediately check whether
         * maintenance task should be generated.
         */
        maintenanceTaskService
                .generateIfNeeded(machine);


        return savedLog;
    }


    public List<UsageLog> getAllLogs() {

        return usageLogRepository.findAll();
    }


    public List<UsageLog> getLogsForMachine(
            Long machineId) {

        if (!machineRepository.existsById(machineId)) {

            throw new ResourceNotFoundException(
                    "Machine with ID "
                            + machineId
                            + " not found."
            );
        }

        return usageLogRepository
                .findByMachineIdOrderByLoggedAtDesc(
                        machineId
                );
    }
}