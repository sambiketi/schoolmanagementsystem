package com.school.management.service;

import com.school.management.domain.Report;
import com.school.management.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    @Transactional
    public Report createDraft(String reportType, String title, String content,
                               Long fromUserId, Long fromDepartmentId) {
        Report report = Report.builder()
                .reportType(reportType)
                .title(title)
                .content(content)
                .fromUserId(fromUserId)
                .fromDepartmentId(fromDepartmentId)
                .status("DRAFT")
                .priority("NORMAL")
                .createdAt(LocalDateTime.now())
                .build();
        return reportRepository.save(report);
    }

    @Transactional
    public Report submit(Long reportId) {
        Report report = getOrThrow(reportId);
        report.setStatus("SUBMITTED");
        report.setSubmissionDate(LocalDate.now());
        report.setUpdatedAt(LocalDateTime.now());
        return reportRepository.save(report);
    }

    @Transactional
    public Report review(Long reportId, Long reviewedBy, String notes, boolean approved) {
        Report report = getOrThrow(reportId);
        report.setStatus(approved ? "APPROVED" : "REJECTED");
        report.setReviewedBy(reviewedBy);
        report.setReviewNotes(notes);
        report.setReviewDate(LocalDate.now());
        report.setUpdatedAt(LocalDateTime.now());
        return reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public Optional<Report> findById(Long id) {
        return reportRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Report> getByUser(Long userId) {
        return reportRepository.findByFromUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<Report> getByDepartment(Long departmentId) {
        return reportRepository.findByFromDepartmentIdOrderByCreatedAtDesc(departmentId);
    }

    @Transactional(readOnly = true)
    public List<Report> getPendingReview() {
        return reportRepository.findByStatus("SUBMITTED");
    }

    private Report getOrThrow(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found: " + id));
    }
}
