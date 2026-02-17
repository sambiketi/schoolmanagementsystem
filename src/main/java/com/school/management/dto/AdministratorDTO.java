package com.school.management.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AdministratorDTO {
    private Long id;
    private Long userId;
    private String adminRole;
    private Long departmentId;
    private String departmentName;
    private boolean canManageUsers = false;
    private boolean canManageDepartments = false;
    private boolean canViewAllData = false;
    private boolean canGenerateReports = false;
    private boolean canManageBackups = false;
    private String officeNumber;
    private String officialTitle;
    private String extension;
    private String digitalSignaturePath;
    private LocalDate signatureAuthorizedDate;
    private boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // User fields (for display)
    private String displayId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String userType;
    
    public AdministratorDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getAdminRole() { return adminRole; }
    public void setAdminRole(String adminRole) { this.adminRole = adminRole; }
    
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    
    public boolean isCanManageUsers() { return canManageUsers; }
    public void setCanManageUsers(boolean canManageUsers) { this.canManageUsers = canManageUsers; }
    
    public boolean isCanManageDepartments() { return canManageDepartments; }
    public void setCanManageDepartments(boolean canManageDepartments) { this.canManageDepartments = canManageDepartments; }
    
    public boolean isCanViewAllData() { return canViewAllData; }
    public void setCanViewAllData(boolean canViewAllData) { this.canViewAllData = canViewAllData; }
    
    public boolean isCanGenerateReports() { return canGenerateReports; }
    public void setCanGenerateReports(boolean canGenerateReports) { this.canGenerateReports = canGenerateReports; }
    
    public boolean isCanManageBackups() { return canManageBackups; }
    public void setCanManageBackups(boolean canManageBackups) { this.canManageBackups = canManageBackups; }
    
    public String getOfficeNumber() { return officeNumber; }
    public void setOfficeNumber(String officeNumber) { this.officeNumber = officeNumber; }
    
    public String getOfficialTitle() { return officialTitle; }
    public void setOfficialTitle(String officialTitle) { this.officialTitle = officialTitle; }
    
    public String getExtension() { return extension; }
    public void setExtension(String extension) { this.extension = extension; }
    
    public String getDigitalSignaturePath() { return digitalSignaturePath; }
    public void setDigitalSignaturePath(String digitalSignaturePath) { this.digitalSignaturePath = digitalSignaturePath; }
    
    public LocalDate getSignatureAuthorizedDate() { return signatureAuthorizedDate; }
    public void setSignatureAuthorizedDate(LocalDate signatureAuthorizedDate) { this.signatureAuthorizedDate = signatureAuthorizedDate; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getDisplayId() { return displayId; }
    public void setDisplayId(String displayId) { this.displayId = displayId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
}