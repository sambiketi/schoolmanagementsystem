package com.school.management.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class GradeEntryDTO {
    
    @NotNull(message = "Class/Group is required")
    private String classGroup;
    
    @NotNull(message = "Subject is required")
    private String subjectName;
    
    @NotNull(message = "Academic year is required")
    private Integer academicYear;
    
    @NotNull(message = "Term is required")
    @Min(value = 1, message = "Term must be between 1 and 3")
    @Max(value = 3, message = "Term must be between 1 and 3")
    private Integer termNumber;
    
    @NotNull(message = "Grading date is required")
    private LocalDate gradingDate;
    
    private String gradeType = "LETTER"; // LETTER, PERCENTAGE, DESCRIPTIVE
    
    private String maxPossibleValue = "A";
    
    private List<StudentGradeDTO> studentGrades;
    
    // Inner class for individual student grades
    public static class StudentGradeDTO {
        
        @NotNull(message = "Student ID is required")
        private Long studentId;
        
        private String studentName;
        
        @NotBlank(message = "Grade is required")
        private String gradeValue;
        
        private String comments;
        
        // Getters and Setters
        public Long getStudentId() {
            return studentId;
        }
        
        public void setStudentId(Long studentId) {
            this.studentId = studentId;
        }
        
        public String getStudentName() {
            return studentName;
        }
        
        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }
        
        public String getGradeValue() {
            return gradeValue;
        }
        
        public void setGradeValue(String gradeValue) {
            this.gradeValue = gradeValue;
        }
        
        public String getComments() {
            return comments;
        }
        
        public void setComments(String comments) {
            this.comments = comments;
        }
    }
    
    // Getters and Setters
    public String getClassGroup() {
        return classGroup;
    }
    
    public void setClassGroup(String classGroup) {
        this.classGroup = classGroup;
    }
    
    public String getSubjectName() {
        return subjectName;
    }
    
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    public Integer getAcademicYear() {
        return academicYear;
    }
    
    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }
    
    public Integer getTermNumber() {
        return termNumber;
    }
    
    public void setTermNumber(Integer termNumber) {
        this.termNumber = termNumber;
    }
    
    public LocalDate getGradingDate() {
        return gradingDate;
    }
    
    public void setGradingDate(LocalDate gradingDate) {
        this.gradingDate = gradingDate;
    }
    
    public String getGradeType() {
        return gradeType;
    }
    
    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }
    
    public String getMaxPossibleValue() {
        return maxPossibleValue;
    }
    
    public void setMaxPossibleValue(String maxPossibleValue) {
        this.maxPossibleValue = maxPossibleValue;
    }
    
    public List<StudentGradeDTO> getStudentGrades() {
        return studentGrades;
    }
    
    public void setStudentGrades(List<StudentGradeDTO> studentGrades) {
        this.studentGrades = studentGrades;
    }
}