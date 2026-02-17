package com.school.management.service;

import com.school.management.domain.LoginLog;
import com.school.management.repository.LoginLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginLogService {

    private final LoginLogRepository loginLogRepository;

    @Transactional
    public LoginLog recordLogin(Long userId, String displayId, String userType,
                                String ipAddress, String userAgent, String loginStatus) {
        LoginLog log = LoginLog.builder()
                .userId(userId)
                .displayId(displayId)
                .userType(userType)
                .loginTime(LocalDateTime.now())
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .loginStatus(loginStatus)
                .isSuspicious(false)
                .build();
        return loginLogRepository.save(log);
    }

    @Transactional
    public void recordLogout(Long userId, String logoutReason) {
        Optional<LoginLog> activeSession = loginLogRepository
                .findTopByUserIdAndLogoutTimeIsNullOrderByLoginTimeDesc(userId);
        activeSession.ifPresent(log -> {
            log.setLogoutTime(LocalDateTime.now());
            log.setLogoutReason(logoutReason);
            loginLogRepository.save(log);
        });
    }

    @Transactional
    public void flagSuspicious(Long logId, String reason) {
        loginLogRepository.findById(logId).ifPresent(log -> {
            log.setIsSuspicious(true);
            log.setSuspiciousReason(reason);
            loginLogRepository.save(log);
        });
    }

    public List<LoginLog> getLoginHistory(Long userId) {
        return loginLogRepository.findByUserIdOrderByLoginTimeDesc(userId);
    }

    public List<LoginLog> getFailedLogins(LocalDateTime since) {
        return loginLogRepository.findByLoginStatusAndLoginTimeAfter("FAILED_PASSWORD", since);
    }

    public List<LoginLog> getSuspiciousActivity() {
        return loginLogRepository.findByIsSuspiciousTrue();
    }
}
