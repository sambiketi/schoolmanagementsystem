package com.school.management.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TeacherDTO {
    
    private Long id;
    private String displayId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    
    // Teacher fields
    private String employeeNumber;
    private LocalDate hireDate;
    private String employmentType;
    private LocalDate contractEndDate;
    private String salaryGrade;
    private String qualificationSummary;
    
    private List<String> subjectsCertified;
    private Map<String, Object> classesAssigned;
    private List<Long> departmentAffiliations;
    private List<Map<String, Object>> trainingCertifications;
    
    private String officialEmail;
    private String officeExtension;
    private String officeRoom;
    private LocalDate lastTrainingDate;
    private LocalDate nextTrainingDate;
    private Integer teachingHoursPerWeek;
    private Integer administrativeHoursPerWeek;
    private String employmentStatus;
    private Integer leaveBalanceDays;
    
    // Department info
    private Long primaryDepartmentId;
    private String primaryDepartmentName;
    
    // User account fields
    private Boolean isActive;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    
    // Constructors
    public TeacherDTO() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDisplayId() {
        return displayId;
    }
    
    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getEmployeeNumber() {
        return employeeNumber;
    }
    
    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    
    public String getEmploymentType() {
        return employmentType;
    }
    
    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }
    
    public LocalDate getContractEndDate() {
        return contractEndDate;
    }
    
    public void setContractEndDate(LocalDate contractEndDate) {
        this.contractEndDate = contractEndDate;
    }
    
    public String getSalaryGrade() {
        return salaryGrade;
    }
    
    public void setSalaryGrade(String salaryGrade) {
        this.salaryGrade = salaryGrade;
    }
    
    public String getQualificationSummary() {
        return qualificationSummary;
    }
    
    public void setQualificationSummary(String qualificationSummary) {
        this.qualificationSummary = qualificationSummary;
    }
    
    public List<String> getSubjectsCertified() {
        return subjectsCertified;
    }
    
    public void setSubjectsCertified(List<String> subjectsCertified) {
        this.subjectsCertified = subjectsCertified;
    }
    
    public Map<String, Object> getClassesAssigned() {
        return classesAssigned;
    }
    
    public void setClassesAssigned(Map<String, Object> classesAssigned) {
        this.classesAssigned = classesAssigned;
    }
    
    public List<Long> getDepartmentAffiliations() {
        return departmentAffiliations;
    }
    
    public void setDepartmentAffiliations(List<Long> departmentAffiliations) {
        this.departmentAffiliations = departmentAffiliations;
    }
    
    public List<Map<String, Object>> getTrainingCertifications() {
        return trainingCertifications;
    }
    
    public void setTrainingCertifications(List<Map<String, Object>> trainingCertifications) {
        this.trainingCertifications = trainingCertifications;
    }
    
    public String getOfficialEmail() {
        return officialEmail;
    }
    
    public void setOfficialEmail(String officialEmail) {
        this.officialEmail = officialEmail;
    }
    
    public String getOfficeExtension() {
        return officeExtension;
    }
    
    public void setOfficeExtension(String officeExtension) {
        this.officeExtension = officeExtension;
    }
    
    public String getOfficeRoom() {
        return officeRoom;
    }
    
    public void setOfficeRoom(String officeRoom) {
        this.officeRoom = officeRoom;
    }
    
    public LocalDate getLastTrainingDate() {
        return lastTrainingDate;
    }
    
    public void setLastTrainingDate(LocalDate lastTrainingDate) {
        this.lastTrainingDate = lastTrainingDate;
    }
    
    public LocalDate getNextTrainingDate() {
        return nextTrainingDate;
    }
    
    public void setNextTrainingDate(LocalDate nextTrainingDate) {
        this.nextTrainingDate = nextTrainingDate;
    }
    
    public Integer getTeachingHoursPerWeek() {
        return teachingHoursPerWeek;
    }
    
    public void setTeachingHoursPerWeek(Integer teachingHoursPerWeek) {
        this.teachingHoursPerWeek = teachingHoursPerWeek;
    }
    
    public Integer getAdministrativeHoursPerWeek() {
        return administrativeHoursPerWeek;
    }
    
    public void setAdministrativeHoursPerWeek(Integer administrativeHoursPerWeek) {
        this.administrativeHoursPerWeek = administrativeHoursPerWeek;
    }
    
    public String getEmploymentStatus() {
        return employmentStatus;
    }
    
    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }
    
    public Integer getLeaveBalanceDays() {
        return leaveBalanceDays;
    }
    
    public void setLeaveBalanceDays(Integer leaveBalanceDays) {
        this.leaveBalanceDays = leaveBalanceDays;
    }
    
    public Long getPrimaryDepartmentId() {
        return primaryDepartmentId;
    }
    
    public void setPrimaryDepartmentId(Long primaryDepartmentId) {
        this.primaryDepartmentId = primaryDepartmentId;
    }
    
    public String getPrimaryDepartmentName() {
        return primaryDepartmentName;
    }
    
    public void setPrimaryDepartmentName(String primaryDepartmentName) {
        this.primaryDepartmentName = primaryDepartmentName;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}