package com.school.management.repository;

import com.school.management.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByFromUserIdOrderByCreatedAtDesc(Long fromUserId);
    List<Report> findByToUserIdOrderByCreatedAtDesc(Long toUserId);
    List<Report> findByFromDepartmentIdOrderByCreatedAtDesc(Long fromDepartmentId);
    List<Report> findByToDepartmentIdOrderByCreatedAtDesc(Long toDepartmentId);
    List<Report> findByStatus(String status);
    List<Report> findByReportTypeAndStatus(String reportType, String status);
    List<Report> findByPriority(String priority);
}
