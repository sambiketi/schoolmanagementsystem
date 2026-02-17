package com.school.management.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "backup_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackupLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "backup_type", length = 20, nullable = false)
    private String backupType; // FULL, INCREMENTAL, DEPARTMENT, EMERGENCY

    @Column(name = "backup_name", length = 255, nullable = false)
    private String backupName;

    @Column(name = "departments_included", columnDefinition = "jsonb")
    private String departmentsIncluded;

    @Column(name = "tables_included", columnDefinition = "jsonb")
    private String tablesIncluded;

    @Column(name = "storage_path", length = 500, nullable = false)
    private String storagePath;

    @Column(name = "backup_size_bytes")
    private Long backupSizeBytes;

    @Column(name = "compression_ratio")
    private Double compressionRatio;

    @Column(name = "checksum", length = 64, nullable = false)
    private String checksum;

    @Column(name = "verification_status", length = 20)
    private String verificationStatus; // PENDING, VERIFIED, FAILED, CORRUPT

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "retention_days", nullable = false)
    private Integer retentionDays;

    @Column(name = "scheduled_deletion_date", insertable = false, updatable = false)
    private LocalDate scheduledDeletionDate;

    @Column(name = "backup_date")
    private LocalDateTime backupDate;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "duration", insertable = false, updatable = false)
    private String duration;

    @Column(name = "status", length = 20, nullable = false)
    private String status; // STARTED, COMPLETED, FAILED, CANCELLED

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "initiated_by")
    private Long initiatedBy;

    @Column(name = "initiated_type", length = 20)
    private String initiatedType; // AUTOMATED, MANUAL
}
