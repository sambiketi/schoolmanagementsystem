package com.school.management.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Entity
@Table(name = "classes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolClass {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "class_code", unique = true, nullable = false, length = 20)
    private String classCode;
    
    @Column(name = "class_name", nullable = false, length = 100)
    private String className;
    
    @Column(name = "academic_year", nullable = false)
    private Integer academicYear;
    
    @ManyToOne
    @JoinColumn(name = "class_teacher_id")
    private User classTeacher;
    
    @ManyToOne
    @JoinColumn(name = "assistant_teacher_id")
    private User assistantTeacher;
    
    @Column(name = "max_students")
    private Integer maxStudents;
    
    @Column(name = "current_students_count")
    private Integer currentStudentsCount;
    
    @Column(name = "classroom_number", length = 20)
    private String classroomNumber;
    
    @Column(name = "building", length = 50)
    private String building;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (isActive == null) {
            isActive = true;
        }
        if (maxStudents == null) {
            maxStudents = 40;
        }
        if (currentStudentsCount == null) {
            currentStudentsCount = 0;
        }
    }
    
    // Helper method to check if class has capacity
    public boolean hasCapacity() {
        return currentStudentsCount < maxStudents;
    }
    
    // Helper method to add student (increment count)
    public void addStudent() {
        if (hasCapacity()) {
            currentStudentsCount++;
        } else {
            throw new IllegalStateException("Class " + classCode + " is at full capacity");
        }
    }
    
    // Helper method to remove student (decrement count)
    public void removeStudent() {
        if (currentStudentsCount > 0) {
            currentStudentsCount--;
        }
    }
    
    // Helper method to get available slots
    public int getAvailableSlots() {
        return maxStudents - currentStudentsCount;
    }
}