package com.example.MaintainIt.repository;

import com.example.MaintainIt.model.MaintenanceTask;
import com.example.MaintainIt.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaintenanceTaskRepository
        extends JpaRepository<MaintenanceTask, Long> {

    boolean existsByMachineIdAndStatus(
            Long machineId,
            TaskStatus status
    );

    Optional<MaintenanceTask>
    findFirstByMachineIdAndStatusOrderByCreatedAtDesc(
            Long machineId,
            TaskStatus status
    );

    List<MaintenanceTask>
    findByStatusOrderByCreatedAtDesc(
            TaskStatus status
    );
}