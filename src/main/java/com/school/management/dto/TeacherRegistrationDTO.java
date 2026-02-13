package com.school.management.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TeacherRegistrationDTO {
    
    // User fields
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    
    // Teacher-specific fields
    @NotBlank(message = "Employee number is required")
    private String employeeNumber;
    
    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;
    
    @NotBlank(message = "Employment type is required")
    private String employmentType; // PERMANENT, CONTRACT, PART_TIME, VISITING
    
    private LocalDate contractEndDate;
    private String salaryGrade;
    private String qualificationSummary;
    
    private List<String> subjectsCertified; // Will be converted to JSON
    private List<Long> departmentAffiliations; // Will be converted to JSON
    
    private String officialEmail;
    private String officeExtension;
    private String officeRoom;
    
    private Integer teachingHoursPerWeek;
    private Integer administrativeHoursPerWeek;
    
    // Getters and Setters
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
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
    
    public List<Long> getDepartmentAffiliations() {
        return departmentAffiliations;
    }
    
    public void setDepartmentAffiliations(List<Long> departmentAffiliations) {
        this.departmentAffiliations = departmentAffiliations;
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
}