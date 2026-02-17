package com.school.management.dto;

import java.time.LocalDateTime;

public class DepartmentDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private boolean canCreateSpreadsheets = true;
    private boolean canSubmitReports = true;
    private Integer maxSpreadsheets = 10;
    private Integer dataRetentionDays = 365;
    private Long departmentHeadId;
    private boolean isActive = true;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    
    public DepartmentDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public boolean isCanCreateSpreadsheets() { return canCreateSpreadsheets; }
    public void setCanCreateSpreadsheets(boolean canCreateSpreadsheets) { this.canCreateSpreadsheets = canCreateSpreadsheets; }
    
    public boolean isCanSubmitReports() { return canSubmitReports; }
    public void setCanSubmitReports(boolean canSubmitReports) { this.canSubmitReports = canSubmitReports; }
    
    public Integer getMaxSpreadsheets() { return maxSpreadsheets; }
    public void setMaxSpreadsheets(Integer maxSpreadsheets) { this.maxSpreadsheets = maxSpreadsheets; }
    
    public Integer getDataRetentionDays() { return dataRetentionDays; }
    public void setDataRetentionDays(Integer dataRetentionDays) { this.dataRetentionDays = dataRetentionDays; }
    
    public Long getDepartmentHeadId() { return departmentHeadId; }
    public void setDepartmentHeadId(Long departmentHeadId) { this.departmentHeadId = departmentHeadId; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Long getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }
}