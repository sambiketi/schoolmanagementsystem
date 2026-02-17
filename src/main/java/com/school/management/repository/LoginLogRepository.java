package com.school.management.repository;

import com.school.management.domain.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
    List<LoginLog> findByUserIdOrderByLoginTimeDesc(Long userId);
    List<LoginLog> findByDisplayIdOrderByLoginTimeDesc(String displayId);
    List<LoginLog> findByLoginStatusAndLoginTimeAfter(String loginStatus, LocalDateTime after);
    List<LoginLog> findByIsSuspiciousTrue();
    Optional<LoginLog> findTopByUserIdAndLogoutTimeIsNullOrderByLoginTimeDesc(Long userId);
}
