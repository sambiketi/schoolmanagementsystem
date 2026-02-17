package com.school.management.service;

import com.school.management.domain.AuditLog;
import com.school.management.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Transactional(readOnly = true)
    public List<AuditLog> getHistoryForRecord(String tableName, Long recordId) {
        return auditLogRepository.findByTableNameAndRecordId(tableName, recordId);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getChangesByUser(Long userId) {
        return auditLogRepository.findByChangedByOrderByChangedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getRecentChanges(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return auditLogRepository.findByChangedAtAfter(since);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getDeletedRecords(String tableName) {
        return auditLogRepository.findByTableNameAndOperation(tableName, "DELETE");
    }
}
