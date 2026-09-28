package com.example.MaintainIt.service;

import com.example.MaintainIt.dto.CompleteTaskRequest;
import com.example.MaintainIt.exception.BusinessRuleException;
import com.example.MaintainIt.exception.ResourceNotFoundException;
import com.example.MaintainIt.model.Machine;
import com.example.MaintainIt.model.MaintenanceIntervalType;
import com.example.MaintainIt.model.MaintenanceTask;
import com.example.MaintainIt.model.TaskStatus;
import com.example.MaintainIt.model.Technician;
import com.example.MaintainIt.repository.MachineRepository;
import com.example.MaintainIt.repository.MaintenanceTaskRepository;
import com.example.MaintainIt.repository.TechnicianRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceTaskService {

    private final MaintenanceTaskRepository taskRepository;
    private final MachineRepository machineRepository;
    private final TechnicianRepository technicianRepository;

    public MaintenanceTaskService(
            MaintenanceTaskRepository taskRepository,
            MachineRepository machineRepository,
            TechnicianRepository technicianRepository) {

        this.taskRepository = taskRepository;
        this.machineRepository = machineRepository;
        this.technicianRepository = technicianRepository;
    }


    /*
     * Automatically create a task when the machine
     * reaches approximately 90% of its interval.
     */
    @Transactional
    public Optional<MaintenanceTask> generateIfNeeded(
            Machine machine) {

        if (!machine.isActive()) {
            return Optional.empty();
        }


        /*
         * BUSINESS RULE:
         *
         * Do not create another task if an
         * earlier task is still OPEN.
         */
        Optional<MaintenanceTask> existingTask =
                taskRepository
                        .findFirstByMachineIdAndStatusOrderByCreatedAtDesc(
                                machine.getId(),
                                TaskStatus.OPEN
                        );

        if (existingTask.isPresent()) {

            return existingTask;
        }


        if (!isNearMaintenance(machine)) {

            return Optional.empty();
        }


        MaintenanceTask task =
                new MaintenanceTask();

        task.setMachine(machine);

        task.setStatus(TaskStatus.OPEN);

        task.setCreatedAt(
                LocalDateTime.now()
        );


        if (machine.getIntervalType()
                == MaintenanceIntervalType.USAGE_HOURS) {

            double dueUsage =
                    machine.getUsageAtLastMaintenance()
                            + machine.getMaintenanceInterval();

            task.setDueUsageHours(dueUsage);

            task.setTriggerReason(
                    "Machine has reached approximately "
                            + "90% of its usage-hour maintenance interval."
            );

        } else {

            LocalDateTime dueDate =
                    machine.getLastMaintenanceDate()
                            .plusDays(
                                    machine.getMaintenanceInterval()
                            );

            task.setDueDate(dueDate);

            task.setTriggerReason(
                    "Machine is approaching its calendar maintenance interval."
            );
        }


        return Optional.of(
                taskRepository.save(task)
        );
    }


    private boolean isNearMaintenance(
            Machine machine) {

        if (machine.getIntervalType()
                == MaintenanceIntervalType.USAGE_HOURS) {

            double usedSinceMaintenance =
                    machine.getCurrentUsageHours()
                            - machine.getUsageAtLastMaintenance();

            double threshold =
                    machine.getMaintenanceInterval()
                            * 0.90;

            return usedSinceMaintenance >= threshold;
        }


        LocalDateTime thresholdDate =
                machine.getLastMaintenanceDate()
                        .plusDays(
                                (long) Math.ceil(
                                        machine.getMaintenanceInterval()
                                                * 0.90
                                )
                        );

        return !LocalDateTime.now()
                .isBefore(thresholdDate);
    }


    public List<MaintenanceTask> getAllTasks() {

        return taskRepository
                .findAll();
    }


    public List<MaintenanceTask> getOpenTasks() {

        return taskRepository
                .findByStatusOrderByCreatedAtDesc(
                        TaskStatus.OPEN
                );
    }


    public List<MaintenanceTask> getOverdueTasks() {

        return taskRepository
                .findByStatusOrderByCreatedAtDesc(
                        TaskStatus.OPEN
                )
                .stream()
                .filter(this::isOverdue)
                .toList();
    }


    private boolean isOverdue(
            MaintenanceTask task) {

        Machine machine =
                task.getMachine();


        if (machine.getIntervalType()
                == MaintenanceIntervalType.USAGE_HOURS) {

            return task.getDueUsageHours() != null
                    && machine.getCurrentUsageHours()
                    >= task.getDueUsageHours();
        }


        return task.getDueDate() != null
                && !LocalDateTime.now()
                .isBefore(task.getDueDate());
    }


    @Transactional
    public MaintenanceTask completeTask(
            Long taskId,
            CompleteTaskRequest request) {

        MaintenanceTask task =
                taskRepository
                        .findById(taskId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Maintenance task with ID "
                                                + taskId
                                                + " not found."
                                )
                        );


        /*
         * BUSINESS RULE:
         *
         * A completed task cannot be completed again.
         */
        if (task.getStatus()
                == TaskStatus.COMPLETED) {

            throw new BusinessRuleException(
                    "This maintenance task has already been completed."
            );
        }


        Technician technician =
                technicianRepository
                        .findById(request.technicianId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Technician with ID "
                                                + request.technicianId()
                                                + " not found."
                                )
                        );


        LocalDateTime completionTime =
                LocalDateTime.now();


        task.setTechnician(technician);

        task.setNotes(
                request.notes().trim()
        );

        task.setCompletedAt(
                completionTime
        );

        task.setStatus(
                TaskStatus.COMPLETED
        );


        /*
         * IMPORTANT BUSINESS RULE:
         *
         * Completing maintenance resets
         * the maintenance counter.
         */
        Machine machine =
                task.getMachine();

        machine.setLastMaintenanceDate(
                completionTime
        );

        machine.setUsageAtLastMaintenance(
                machine.getCurrentUsageHours()
        );


        machineRepository.save(machine);

        return taskRepository.save(task);
    }


    /*
     * Called automatically by the scheduler.
     */
    @Transactional
    public void autoGenerateForAllMachines() {

        List<Machine> machines =
                machineRepository.findByActiveTrue();

        for (Machine machine : machines) {

            generateIfNeeded(machine);
        }
    }
}