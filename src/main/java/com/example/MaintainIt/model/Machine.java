package com.example.MaintainIt.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "machines")
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String machineName;

    @Column(nullable = false, unique = true)
    private String machineCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceIntervalType intervalType;

    @Column(nullable = false)
    private Integer maintenanceInterval;

    @Column(nullable = false)
    private Double currentUsageHours = 0.0;

    @Column(nullable = false)
    private Double usageAtLastMaintenance = 0.0;

    @Column(nullable = false)
    private LocalDateTime lastMaintenanceDate;

    @Column(nullable = false)
    private boolean active = true;

    public Machine() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public MaintenanceIntervalType getIntervalType() {
        return intervalType;
    }

    public void setIntervalType(MaintenanceIntervalType intervalType) {
        this.intervalType = intervalType;
    }

    public Integer getMaintenanceInterval() {
        return maintenanceInterval;
    }

    public void setMaintenanceInterval(Integer maintenanceInterval) {
        this.maintenanceInterval = maintenanceInterval;
    }

    public Double getCurrentUsageHours() {
        return currentUsageHours;
    }

    public void setCurrentUsageHours(Double currentUsageHours) {
        this.currentUsageHours = currentUsageHours;
    }

    public Double getUsageAtLastMaintenance() {
        return usageAtLastMaintenance;
    }

    public void setUsageAtLastMaintenance(Double usageAtLastMaintenance) {
        this.usageAtLastMaintenance = usageAtLastMaintenance;
    }

    public LocalDateTime getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDateTime lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}