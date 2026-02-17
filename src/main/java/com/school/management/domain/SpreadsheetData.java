package com.school.management.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "spreadsheet_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpreadsheetData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spreadsheet_id", nullable = false)
    private Long spreadsheetId;

    @Column(name = "row_data", columnDefinition = "jsonb", nullable = false)
    private String rowData;

    @Column(name = "row_hash", length = 64, nullable = false)
    private String rowHash; // DB trigger overwrites this automatically

    @Column(name = "row_status", length = 20)
    private String rowStatus; // ACTIVE, ARCHIVED, DELETED

    @Column(name = "version")
    private Integer version;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;
}
