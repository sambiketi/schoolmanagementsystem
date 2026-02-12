package com.school.management.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "display_id", unique = true)
    private String displayId;
    
    @Column(name = "display_id_prefix", length = 3, nullable = false)
    private String displayIdPrefix;
    
    @Column(name = "display_id_year", nullable = false)
    private Integer displayIdYear;
    
    @Column(name = "display_id_sequence", nullable = false)
    private Integer displayIdSequence;
    
    @Column(name = "display_id_check", length = 1, nullable = false)
    private String displayIdCheck;
    
    @Column(name = "auth_type", length = 10, nullable = false)
    private String authType;
    
    @Column(name = "username", length = 100, unique = true)
    private String username;
    
    @Column(name = "email", length = 255)
    private String email;
    
    @Column(name = "password_hash", length = 255)
    private String passwordHash;
    
    @Column(name = "full_name", length = 255, nullable = false)
    private String fullName;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(name = "gender", length = 10)
    private String gender;
    
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
    
    @Column(name = "user_type", length = 20, nullable = false)
    private String userType;
    
    @Column(name = "role_subtype", length = 50)
    private String roleSubtype;
    
    @Column(name = "department_id")
    private Long departmentId;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked;
    
    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts;
    
    @Column(name = "must_change_password")
    private Boolean mustChangePassword;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @Column(name = "last_password_change")
    private LocalDateTime lastPasswordChange;
    
    @Column(name = "login_count")
    private Integer loginCount;
    
    @Column(name = "current_session_id", length = 100)
    private String currentSessionId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "created_by")
    private Long createdBy;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "updated_by")
    private Long updatedBy;
    
    // Relationships
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Student student;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Teacher teacher;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Administrator administrator;
}