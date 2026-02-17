package com.school.management.service;

import com.school.management.domain.SystemAlert;
import com.school.management.repository.SystemAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemAlertService {

    private final SystemAlertRepository systemAlertRepository;

    @Transactional
    public SystemAlert createAlert(String alertType, String severity,
                                    String message, String checkName, String value) {
        SystemAlert alert = SystemAlert.builder()
                .alertType(alertType)
                .severity(severity)
                .message(message)
                .checkName(checkName)
                .value(value)
                .acknowledged(false)
                .createdAt(LocalDateTime.now())
                .build();
        return systemAlertRepository.save(alert);
    }

    @Transactional
    public void acknowledge(Long alertId, Long acknowledgedBy) {
        systemAlertRepository.findById(alertId).ifPresent(alert -> {
            alert.setAcknowledged(true);
            alert.setAcknowledgedBy(acknowledgedBy);
            alert.setAcknowledgedAt(LocalDateTime.now());
            systemAlertRepository.save(alert);
        });
    }

    @Transactional(readOnly = true)
    public List<SystemAlert> getUnacknowledged() {
        return systemAlertRepository.findByAcknowledgedFalseOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<SystemAlert> getCritical() {
        return systemAlertRepository.findBySeverityAndAcknowledgedFalse("CRITICAL");
    }
}
