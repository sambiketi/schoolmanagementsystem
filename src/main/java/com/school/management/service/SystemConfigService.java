package com.school.management.service;

import com.school.management.domain.SystemConfig;
import com.school.management.repository.SystemConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;

    @Transactional(readOnly = true)
    public Optional<String> getValue(String key) {
        return systemConfigRepository.findByConfigKey(key)
                .map(SystemConfig::getConfigValue);
    }

    @Transactional(readOnly = true)
    public Optional<SystemConfig> getConfig(String key) {
        return systemConfigRepository.findByConfigKey(key);
    }

    @Transactional
    public SystemConfig upsert(String key, String value, String type,
                                String scope, String description, Long updatedBy) {
        Optional<SystemConfig> existing = systemConfigRepository.findByConfigKey(key);
        if (existing.isPresent()) {
            SystemConfig config = existing.get();
            config.setConfigValue(value);
            config.setUpdatedAt(LocalDateTime.now());
            config.setUpdatedBy(updatedBy);
            return systemConfigRepository.save(config);
        } else {
            SystemConfig config = SystemConfig.builder()
                    .configKey(key)
                    .configValue(value)
                    .configType(type)
                    .configScope(scope != null ? scope : "GLOBAL")
                    .description(description)
                    .version(1)
                    .isEncrypted(false)
                    .createdAt(LocalDateTime.now())
                    .createdBy(updatedBy)
                    .build();
            return systemConfigRepository.save(config);
        }
    }

    @Transactional(readOnly = true)
    public List<SystemConfig> getByScope(String scope) {
        return systemConfigRepository.findByConfigScope(scope);
    }
}
