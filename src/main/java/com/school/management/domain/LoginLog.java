package com.school.management.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "display_id", length = 20)
    private String displayId;

    @Column(name = "user_type", length = 20)
    private String userType;

    @Column(name = "login_time")
    private LocalDateTime loginTime;

    @Column(name = "logout_time")
    private LocalDateTime logoutTime;

    @Column(name = "session_duration", insertable = false, updatable = false)
    private String sessionDuration;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "mac_address", length = 17)
    private String macAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "computer_name", length = 100)
    private String computerName;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "login_status", length = 20, nullable = false)
    private String loginStatus; // SUCCESS, FAILED_ID, FAILED_PASSWORD, LOCKED, EXPIRED

    @Column(name = "logout_reason", length = 50)
    private String logoutReason; // USER, TIMEOUT, ADMIN, END_OF_DAY, SYSTEM

    @Column(name = "is_suspicious")
    private Boolean isSuspicious;

    @Column(name = "suspicious_reason", columnDefinition = "TEXT")
    private String suspiciousReason;
}
