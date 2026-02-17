package com.school.management.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "maintenance_windows")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceWindow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "window_name", length = 100, nullable = false)
    private String windowName;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek; // 0=Sunday, 6=Saturday

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "tasks_completed", columnDefinition = "jsonb")
    private String tasksCompleted;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
