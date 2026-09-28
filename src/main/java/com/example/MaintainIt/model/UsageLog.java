package com.example.MaintainIt.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usage_logs")
public class UsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @Column(nullable = false)
    private Double hoursUsed;

    @Column(nullable = false)
    private Double cumulativeUsageHours;

    @Column(nullable = false)
    private LocalDateTime loggedAt;

    public UsageLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Double getHoursUsed() {
        return hoursUsed;
    }

    public void setHoursUsed(Double hoursUsed) {
        this.hoursUsed = hoursUsed;
    }

    public Double getCumulativeUsageHours() {
        return cumulativeUsageHours;
    }

    public void setCumulativeUsageHours(Double cumulativeUsageHours) {
        this.cumulativeUsageHours = cumulativeUsageHours;
    }

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(LocalDateTime loggedAt) {
        this.loggedAt = loggedAt;
    }
}