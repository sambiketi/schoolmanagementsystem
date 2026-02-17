package com.school.management.repository;

import com.school.management.domain.SystemAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemAlertRepository extends JpaRepository<SystemAlert, Long> {
    List<SystemAlert> findByAcknowledgedFalseOrderByCreatedAtDesc();
    List<SystemAlert> findBySeverityAndAcknowledgedFalse(String severity);
    List<SystemAlert> findByAlertTypeOrderByCreatedAtDesc(String alertType);
}
