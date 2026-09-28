package com.example.MaintainIt.service;

import com.example.MaintainIt.dto.TechnicianRequest;
import com.example.MaintainIt.exception.BusinessRuleException;
import com.example.MaintainIt.exception.ResourceNotFoundException;
import com.example.MaintainIt.model.Technician;
import com.example.MaintainIt.repository.MaintenanceTaskRepository;
import com.example.MaintainIt.repository.TechnicianRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TechnicianService {

    private final TechnicianRepository technicianRepository;
    private final MaintenanceTaskRepository maintenanceTaskRepository;

    public TechnicianService(
            TechnicianRepository technicianRepository,
            MaintenanceTaskRepository maintenanceTaskRepository) {

        this.technicianRepository = technicianRepository;
        this.maintenanceTaskRepository = maintenanceTaskRepository;
    }

    // CREATE TECHNICIAN
    public Technician createTechnician(
            TechnicianRequest request) {

        if (technicianRepository.existsByPhone(request.phone())) {
            throw new BusinessRuleException(
                    "Technician with this phone number already exists"
            );
        }

        Technician technician = new Technician();

        technician.setName(request.name().trim());
        technician.setEmail(request.email().trim());
        technician.setPhone(request.phone().trim());

        return technicianRepository.save(technician);
    }

    // GET ALL TECHNICIANS
    public List<Technician> getAllTechnicians() {
        return technicianRepository.findAll();
    }

    // DELETE TECHNICIAN
    @Transactional
    public void deleteTechnician(Long id) {

        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Technician not found with ID: " + id
                        )
                );

        /*
         * Maintenance tasks may contain a reference
         * to this technician.
         *
         * Instead of deleting those historical tasks,
         * remove only the technician reference.
         */
        maintenanceTaskRepository.clearTechnician(technician);

        // Now delete the technician
        technicianRepository.delete(technician);
    }
}