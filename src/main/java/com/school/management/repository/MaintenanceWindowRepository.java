package com.school.management.repository;

import com.school.management.domain.MaintenanceWindow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaintenanceWindowRepository extends JpaRepository<MaintenanceWindow, Long> {
    List<MaintenanceWindow> findByIsActiveTrue();
    Optional<MaintenanceWindow> findByDayOfWeekAndIsActiveTrue(Integer dayOfWeek);
}
