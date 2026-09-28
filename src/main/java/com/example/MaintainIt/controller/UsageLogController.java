package com.example.MaintainIt.controller;

import com.example.MaintainIt.dto.UsageLogRequest;
import com.example.MaintainIt.model.UsageLog;
import com.example.MaintainIt.service.UsageLogService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usage-logs")
public class UsageLogController {

    private final UsageLogService usageLogService;

    public UsageLogController(
            UsageLogService usageLogService) {

        this.usageLogService =
                usageLogService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsageLog logUsage(
            @Valid @RequestBody UsageLogRequest request) {

        return usageLogService.logUsage(
                request
        );
    }


    @GetMapping
    public List<UsageLog> getAllLogs() {

        return usageLogService.getAllLogs();
    }


    @GetMapping("/machine/{machineId}")
    public List<UsageLog> getMachineLogs(
            @PathVariable Long machineId) {

        return usageLogService
                .getLogsForMachine(machineId);
    }
}