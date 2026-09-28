package com.example.MaintainIt.repository;

import com.example.MaintainIt.model.Machine;
import com.example.MaintainIt.model.UsageLog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsageLogRepository
        extends JpaRepository<UsageLog, Long> {

    // Existing method
    List<UsageLog> findByMachineIdOrderByLoggedAtDesc(
            Long machineId
    );

    // New method for machine deletion
    void deleteAllByMachine(Machine machine);
}