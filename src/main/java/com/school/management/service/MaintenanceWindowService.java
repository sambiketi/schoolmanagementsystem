package com.school.management.service;

import com.school.management.domain.MaintenanceWindow;
import com.school.management.repository.MaintenanceWindowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaintenanceWindowService {

    private final MaintenanceWindowRepository maintenanceWindowRepository;

    @Transactional(readOnly = true)
    public boolean isMaintenanceNow() {
        int dayOfWeek = LocalDateTime.now().getDayOfWeek().getValue() % 7;
        Optional<MaintenanceWindow> window = maintenanceWindowRepository
                .findByDayOfWeekAndIsActiveTrue(dayOfWeek);
        return window.map(w -> {
            var now = LocalDateTime.now().toLocalTime();
            return !now.isBefore(w.getStartTime()) && !now.isAfter(w.getEndTime());
        }).orElse(false);
    }

    @Transactional(readOnly = true)
    public List<MaintenanceWindow> getActiveWindows() {
        return maintenanceWindowRepository.findByIsActiveTrue();
    }

    @Transactional
    public MaintenanceWindow save(MaintenanceWindow window) {
        if (window.getCreatedAt() == null) {
            window.setCreatedAt(LocalDateTime.now());
        }
        return maintenanceWindowRepository.save(window);
    }

    @Transactional
    public void deactivate(Long id) {
        maintenanceWindowRepository.findById(id).ifPresent(w -> {
            w.setIsActive(false);
            maintenanceWindowRepository.save(w);
        });
    }
}
