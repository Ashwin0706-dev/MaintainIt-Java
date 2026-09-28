package com.example.MaintainIt.controller;

import com.example.MaintainIt.dto.CompleteTaskRequest;
import com.example.MaintainIt.model.MaintenanceTask;
import com.example.MaintainIt.service.MaintenanceTaskService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance-tasks")
public class MaintenanceTaskController {

    private final MaintenanceTaskService taskService;

    public MaintenanceTaskController(
            MaintenanceTaskService taskService) {

        this.taskService =
                taskService;
    }


    @GetMapping
    public List<MaintenanceTask> getAllTasks() {

        return taskService.getAllTasks();
    }


    @GetMapping("/open")
    public List<MaintenanceTask> getOpenTasks() {

        return taskService.getOpenTasks();
    }


    @GetMapping("/overdue")
    public List<MaintenanceTask> getOverdueTasks() {

        return taskService.getOverdueTasks();
    }


    @PutMapping("/{taskId}/complete")
    public MaintenanceTask completeTask(
            @PathVariable Long taskId,
            @Valid @RequestBody
            CompleteTaskRequest request) {

        return taskService.completeTask(
                taskId,
                request
        );
    }
}