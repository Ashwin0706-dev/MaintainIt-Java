package com.example.MaintainIt.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceScheduler {

    private final MaintenanceTaskService maintenanceTaskService;

    public MaintenanceScheduler(
            MaintenanceTaskService maintenanceTaskService) {

        this.maintenanceTaskService =
                maintenanceTaskService;
    }


    /*
     * Runs every 60 seconds.
     *
     * This is especially important for
     * CALENDAR_DAYS maintenance.
     */
    @Scheduled(fixedRate = 60000)
    public void checkMaintenanceSchedules() {

        maintenanceTaskService
                .autoGenerateForAllMachines();
    }
}