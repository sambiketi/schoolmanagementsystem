package com.school.management.repository;

import com.school.management.domain.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByTableNameAndRecordId(String tableName, Long recordId);
    List<AuditLog> findByChangedByOrderByChangedAtDesc(Long changedBy);
    List<AuditLog> findByChangedAtAfter(LocalDateTime after);
    List<AuditLog> findByTableNameAndOperation(String tableName, String operation);
}
