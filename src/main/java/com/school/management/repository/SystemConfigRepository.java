package com.school.management.repository;

import com.school.management.domain.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
    Optional<SystemConfig> findByConfigKey(String configKey);
    List<SystemConfig> findByConfigScope(String configScope);
    List<SystemConfig> findByConfigScopeAndScopeId(String configScope, Long scopeId);
    boolean existsByConfigKey(String configKey);
}
