package com.example.MaintainIt.repository;

import com.example.MaintainIt.model.Machine;
import com.example.MaintainIt.model.MaintenanceTask;
import com.example.MaintainIt.model.Technician;
import com.example.MaintainIt.model.TaskStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaintenanceTaskRepository
        extends JpaRepository<MaintenanceTask, Long> {

    // Existing method required by MaintenanceTaskService
    Optional<MaintenanceTask>
    findFirstByMachineIdAndStatusOrderByCreatedAtDesc(
            Long machineId,
            TaskStatus status
    );

    // Existing methods required by MaintenanceTaskService
    List<MaintenanceTask>
    findByStatusOrderByCreatedAtDesc(TaskStatus status);

    // Delete all tasks belonging to a machine
    void deleteAllByMachine(Machine machine);

    // Remove technician assignment without deleting the task
    @Modifying
    @Query("""
           UPDATE MaintenanceTask m
           SET m.technician = null
           WHERE m.technician = :technician
           """)
    void clearTechnician(
            @Param("technician") Technician technician
    );
}