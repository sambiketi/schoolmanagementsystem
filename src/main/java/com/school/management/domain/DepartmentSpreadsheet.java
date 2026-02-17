package com.school.management.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "department_spreadsheets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentSpreadsheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "spreadsheet_name", length = 100, nullable = false)
    private String spreadsheetName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "columns_json", columnDefinition = "jsonb", nullable = false)
    private String columnsJson;

    @Column(name = "can_edit", columnDefinition = "jsonb")
    private String canEdit;

    @Column(name = "can_view", columnDefinition = "jsonb")
    private String canView;

    @Column(name = "version")
    private Integer version;

    @Column(name = "previous_version_id")
    private Long previousVersionId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "is_active")
    private Boolean isActive;
}
