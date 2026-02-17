package com.school.management.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_type", length = 50, nullable = false)
    private String reportType; // TEACHER_REPORT, DEPT_REPORT, REQUEST, INCIDENT, PERFORMANCE

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "from_user_id", nullable = false)
    private Long fromUserId;

    @Column(name = "from_department_id")
    private Long fromDepartmentId;

    @Column(name = "to_user_id")
    private Long toUserId;

    @Column(name = "to_department_id")
    private Long toDepartmentId;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "attachments_json", columnDefinition = "jsonb")
    private String attachmentsJson;

    @Column(name = "status", length = 20)
    private String status; // DRAFT, SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, ARCHIVED

    @Column(name = "priority", length = 10)
    private String priority; // LOW, NORMAL, HIGH, URGENT

    @Column(name = "submission_date")
    private LocalDate submissionDate;

    @Column(name = "deadline_date")
    private LocalDate deadlineDate;

    @Column(name = "review_date")
    private LocalDate reviewDate;

    @Column(name = "archived_date")
    private LocalDate archivedDate;

    @Column(name = "reviewed_by")
    private Long reviewedBy;

    @Column(name = "review_notes", columnDefinition = "TEXT")
    private String reviewNotes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
