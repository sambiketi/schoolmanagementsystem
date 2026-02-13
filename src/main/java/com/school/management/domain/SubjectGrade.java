package com.school.management.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "subject_grades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectGrade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;
    
    @Column(name = "subject_name", nullable = false, length = 100)
    private String subjectName;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;
    
    @ManyToOne
    @JoinColumn(name = "class_teacher_id")
    private User classTeacher;
    
    @Column(name = "academic_year", nullable = false)
    private Integer academicYear;
    
    @Column(name = "term_number", nullable = false)
    private Integer termNumber;
    
    @Column(name = "grading_date", nullable = false)
    private LocalDate gradingDate;
    
    @Column(name = "grade_value", length = 10)
    private String gradeValue;
    
    @Column(name = "grade_type", length = 20, nullable = false)
    private String gradeType; // LETTER, PERCENTAGE, DESCRIPTIVE
    
    @Column(name = "max_possible_value", length = 10)
    private String maxPossibleValue;
    
    @Column(name = "class_rank")
    private Integer classRank;
    
    @Column(name = "total_in_class")
    private Integer totalInClass;
    
    @Column(name = "teacher_comments", columnDefinition = "TEXT")
    private String teacherComments;
    
    @Column(name = "class_teacher_comments", columnDefinition = "TEXT")
    private String classTeacherComments;
    
    @Column(name = "submission_status", length = 20, nullable = false)
    private String submissionStatus; // DRAFT, SUBMITTED, REVIEWED, APPROVED, REJECTED
    
    @Column(name = "forwarded_to_class_teacher")
    private Boolean forwardedToClassTeacher;
    
    @Column(name = "class_teacher_reviewed")
    private Boolean classTeacherReviewed;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (submissionStatus == null) {
            submissionStatus = "DRAFT";
        }
        if (forwardedToClassTeacher == null) {
            forwardedToClassTeacher = false;
        }
        if (classTeacherReviewed == null) {
            classTeacherReviewed = false;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}