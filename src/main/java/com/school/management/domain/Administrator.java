package com.school.management.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "administrators")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Administrator {
    
    @Id
    private Long id;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "admin_role", length = 50, nullable = false)
    private String adminRole;
    
    @Column(name = "department_id")
    private Long departmentId;
    
    @Column(name = "can_manage_users")
    private Boolean canManageUsers;
    
    @Column(name = "can_manage_departments")
    private Boolean canManageDepartments;
    
    @Column(name = "can_view_all_data")
    private Boolean canViewAllData;
    
    @Column(name = "can_generate_reports")
    private Boolean canGenerateReports;
    
    @Column(name = "can_manage_backups")
    private Boolean canManageBackups;
    
    @Column(name = "office_number", length = 50)
    private String officeNumber;
    
    @Column(name = "official_title", length = 100)
    private String officialTitle;
    
    @Column(name = "extension", length = 10)
    private String extension;
    
    @Column(name = "digital_signature_path", length = 255)
    private String digitalSignaturePath;
    
    @Column(name = "signature_authorized_date")
    private LocalDate signatureAuthorizedDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}