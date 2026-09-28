package com.example.MaintainIt.repository;

import com.example.MaintainIt.model.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {

    boolean existsByMachineCode(String machineCode);

    List<Machine> findByActiveTrue();
}