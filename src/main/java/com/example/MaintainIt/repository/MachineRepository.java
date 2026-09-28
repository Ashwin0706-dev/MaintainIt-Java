package com.example.MaintainIt.repository;

import com.example.MaintainIt.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MachineRepository
        extends JpaRepository<Machine, Long> {

    Optional<Machine> findByMachineCodeIgnoreCase(String machineCode);

    boolean existsByMachineCodeIgnoreCase(String machineCode);

    List<Machine> findByActiveTrue();
}