package com.school.management.repository;

import com.school.management.domain.BackupLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BackupLogRepository extends JpaRepository<BackupLog, Long> {
    Optional<BackupLog> findTopByStatusOrderByBackupDateDesc(String status);
    List<BackupLog> findByStatusOrderByBackupDateDesc(String status);
    List<BackupLog> findByBackupTypeAndBackupDateAfter(String backupType, LocalDateTime after);
    List<BackupLog> findByVerificationStatus(String verificationStatus);
}
