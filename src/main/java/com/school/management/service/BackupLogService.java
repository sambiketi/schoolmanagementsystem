package com.school.management.service;

import com.school.management.domain.BackupLog;
import com.school.management.repository.BackupLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BackupLogService {

    private final BackupLogRepository backupLogRepository;

    @Transactional
    public BackupLog startBackup(String backupType, String backupName,
                                  String storagePath, Long initiatedBy, String initiatedType) {
        BackupLog log = BackupLog.builder()
                .backupType(backupType)
                .backupName(backupName)
                .storagePath(storagePath)
                .checksum("PENDING")
                .retentionDays(30)
                .backupDate(LocalDateTime.now())
                .startedAt(LocalDateTime.now())
                .status("STARTED")
                .verificationStatus("PENDING")
                .initiatedBy(initiatedBy)
                .initiatedType(initiatedType)
                .build();
        return backupLogRepository.save(log);
    }

    @Transactional
    public BackupLog completeBackup(Long backupId, Long sizeBytes,
                                     String checksum, Double compressionRatio) {
        BackupLog log = backupLogRepository.findById(backupId)
                .orElseThrow(() -> new RuntimeException("Backup log not found: " + backupId));
        log.setStatus("COMPLETED");
        log.setCompletedAt(LocalDateTime.now());
        log.setBackupSizeBytes(sizeBytes);
        log.setChecksum(checksum);
        log.setCompressionRatio(compressionRatio);
        return backupLogRepository.save(log);
    }

    @Transactional
    public BackupLog failBackup(Long backupId, String errorMessage) {
        BackupLog log = backupLogRepository.findById(backupId)
                .orElseThrow(() -> new RuntimeException("Backup log not found: " + backupId));
        log.setStatus("FAILED");
        log.setCompletedAt(LocalDateTime.now());
        log.setErrorMessage(errorMessage);
        return backupLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public Optional<BackupLog> getLastSuccessfulBackup() {
        return backupLogRepository.findTopByStatusOrderByBackupDateDesc("COMPLETED");
    }

    @Transactional(readOnly = true)
    public List<BackupLog> getAllBackups() {
        return backupLogRepository.findByStatusOrderByBackupDateDesc("COMPLETED");
    }
}
