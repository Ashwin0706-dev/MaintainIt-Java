package com.example.MaintainIt.repository;

import com.example.MaintainIt.model.Technician;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicianRepository
        extends JpaRepository<Technician, Long> {

    boolean existsByPhone(String phone);
}