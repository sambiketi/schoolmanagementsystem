package com.school.management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassDTO {
    private Long id;
    private String classCode;
    private String className;
    private Integer academicYear;
    private Long classTeacherId;
    private String classTeacherName;
    private Long assistantTeacherId;
    private String assistantTeacherName;
    private Integer maxStudents;
    private Integer currentStudentsCount;
    private String classroomNumber;
    private String building;
    private Boolean isActive;
    
    // Helper methods
    public int getAvailableSlots() {
        return maxStudents - currentStudentsCount;
    }
    
    public boolean isFull() {
        return currentStudentsCount >= maxStudents;
    }
    
    public String getStatus() {
        if (!isActive) {
            return "INACTIVE";
        } else if (isFull()) {
            return "FULL";
        } else {
            return "ACTIVE";
        }
    }
}