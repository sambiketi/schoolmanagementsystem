package com.school.management.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_key", length = 100, unique = true, nullable = false)
    private String configKey;

    @Column(name = "config_value", columnDefinition = "jsonb", nullable = false)
    private String configValue;

    @Column(name = "config_type", length = 50, nullable = false)
    private String configType; // STRING, NUMBER, BOOLEAN, JSON, ARRAY

    @Column(name = "config_scope", length = 50)
    private String configScope; // GLOBAL, DEPARTMENT, USER, CLASS

    @Column(name = "scope_id")
    private Long scopeId;

    @Column(name = "is_encrypted")
    private Boolean isEncrypted;

    @Column(name = "encryption_key_id", length = 100)
    private String encryptionKeyId;

    @Column(name = "version")
    private Integer version;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;
}
