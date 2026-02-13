package com.school.management.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GradeDTO {
    
    private Long id;
    private Long studentId;
    private String studentName;
    private String studentDisplayId;
    private String subjectName;
    private Long teacherId;
    private String teacherName;
    private Long classTeacherId;
    private String classTeacherName;
    private Integer academicYear;
    private Integer termNumber;
    private LocalDate gradingDate;
    private String gradeValue;
    private String gradeType;
    private String maxPossibleValue;
    private Integer classRank;
    private Integer totalInClass;
    private String teacherComments;
    private String classTeacherComments;
    private String submissionStatus;
    private Boolean forwardedToClassTeacher;
    private Boolean classTeacherReviewed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public GradeDTO() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getStudentDisplayId() {
        return studentDisplayId;
    }
    
    public void setStudentDisplayId(String studentDisplayId) {
        this.studentDisplayId = studentDisplayId;
    }
    
    public String getSubjectName() {
        return subjectName;
    }
    
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    public Long getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
    
    public String getTeacherName() {
        return teacherName;
    }
    
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
    
    public Long getClassTeacherId() {
        return classTeacherId;
    }
    
    public void setClassTeacherId(Long classTeacherId) {
        this.classTeacherId = classTeacherId;
    }
    
    public String getClassTeacherName() {
        return classTeacherName;
    }
    
    public void setClassTeacherName(String classTeacherName) {
        this.classTeacherName = classTeacherName;
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
    
    public String getGradeValue() {
        return gradeValue;
    }
    
    public void setGradeValue(String gradeValue) {
        this.gradeValue = gradeValue;
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
    
    public Integer getClassRank() {
        return classRank;
    }
    
    public void setClassRank(Integer classRank) {
        this.classRank = classRank;
    }
    
    public Integer getTotalInClass() {
        return totalInClass;
    }
    
    public void setTotalInClass(Integer totalInClass) {
        this.totalInClass = totalInClass;
    }
    
    public String getTeacherComments() {
        return teacherComments;
    }
    
    public void setTeacherComments(String teacherComments) {
        this.teacherComments = teacherComments;
    }
    
    public String getClassTeacherComments() {
        return classTeacherComments;
    }
    
    public void setClassTeacherComments(String classTeacherComments) {
        this.classTeacherComments = classTeacherComments;
    }
    
    public String getSubmissionStatus() {
        return submissionStatus;
    }
    
    public void setSubmissionStatus(String submissionStatus) {
        this.submissionStatus = submissionStatus;
    }
    
    public Boolean getForwardedToClassTeacher() {
        return forwardedToClassTeacher;
    }
    
    public void setForwardedToClassTeacher(Boolean forwardedToClassTeacher) {
        this.forwardedToClassTeacher = forwardedToClassTeacher;
    }
    
    public Boolean getClassTeacherReviewed() {
        return classTeacherReviewed;
    }
    
    public void setClassTeacherReviewed(Boolean classTeacherReviewed) {
        this.classTeacherReviewed = classTeacherReviewed;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}