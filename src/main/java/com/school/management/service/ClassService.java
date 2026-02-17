package com.school.management.service;

import com.school.management.domain.SchoolClass;
import com.school.management.domain.User;
import com.school.management.dto.ClassDTO;
import com.school.management.repository.ClassRepository;
import com.school.management.repository.UserRepository;
import lombok.Data;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClassService {
    
    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    
    public ClassService(
            ClassRepository classRepository,
            UserRepository userRepository) {
        this.classRepository = classRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * Get all classes
     */
    public List<ClassDTO> getAllClasses() {
        return classRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get active classes
     */
    public List<ClassDTO> getActiveClasses() {
        return classRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get class by ID
     */
    public ClassDTO getClassById(Long id) {
        return classRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }
    
    /**
     * Get class by code
     */
    public ClassDTO getClassByCode(String code) {
        return classRepository.findByClassCode(code)
                .map(this::convertToDTO)
                .orElse(null);
    }
    
    /**
     * Create new class
     */
    public ClassDTO createClass(ClassDTO classDTO, Long createdById) {
        // Check if class code already exists
        if (classRepository.findByClassCode(classDTO.getClassCode()).isPresent()) {
            throw new RuntimeException("Class code already exists: " + classDTO.getClassCode());
        }
        
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setClassCode(classDTO.getClassCode());
        schoolClass.setClassName(classDTO.getClassName());
        schoolClass.setAcademicYear(classDTO.getAcademicYear());
        schoolClass.setMaxStudents(classDTO.getMaxStudents());
        schoolClass.setCurrentStudentsCount(0); // Start with 0 students
        schoolClass.setClassroomNumber(classDTO.getClassroomNumber());
        schoolClass.setBuilding(classDTO.getBuilding());
        schoolClass.setIsActive(classDTO.getIsActive() != null ? classDTO.getIsActive() : true);
        
        // Set class teacher if provided
        if (classDTO.getClassTeacherId() != null) {
            User classTeacher = userRepository.findById(classDTO.getClassTeacherId())
                    .orElseThrow(() -> new RuntimeException("Class teacher not found with id: " + classDTO.getClassTeacherId()));
            schoolClass.setClassTeacher(classTeacher);
        }
        
        // Set assistant teacher if provided
        if (classDTO.getAssistantTeacherId() != null) {
            User assistantTeacher = userRepository.findById(classDTO.getAssistantTeacherId())
                    .orElseThrow(() -> new RuntimeException("Assistant teacher not found with id: " + classDTO.getAssistantTeacherId()));
            schoolClass.setAssistantTeacher(assistantTeacher);
        }
        
        // Set created by
        User createdBy = userRepository.findById(createdById)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + createdById));
        schoolClass.setCreatedBy(createdBy);
        schoolClass.setCreatedAt(LocalDateTime.now());
        
        SchoolClass saved = classRepository.save(schoolClass);
        return convertToDTO(saved);
    }
    
    /**
     * Update class
     */
    public ClassDTO updateClass(Long id, ClassDTO classDTO) {
        SchoolClass schoolClass = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));
        
        // Update basic fields
        schoolClass.setClassName(classDTO.getClassName());
        schoolClass.setAcademicYear(classDTO.getAcademicYear());
        schoolClass.setMaxStudents(classDTO.getMaxStudents());
        schoolClass.setClassroomNumber(classDTO.getClassroomNumber());
        schoolClass.setBuilding(classDTO.getBuilding());
        
        if (classDTO.getIsActive() != null) {
            schoolClass.setIsActive(classDTO.getIsActive());
        }
        
        // Update class teacher if changed
        if (classDTO.getClassTeacherId() != null && 
            (schoolClass.getClassTeacher() == null || 
             !schoolClass.getClassTeacher().getId().equals(classDTO.getClassTeacherId()))) {
            User classTeacher = userRepository.findById(classDTO.getClassTeacherId())
                    .orElseThrow(() -> new RuntimeException("Class teacher not found with id: " + classDTO.getClassTeacherId()));
            schoolClass.setClassTeacher(classTeacher);
        } else if (classDTO.getClassTeacherId() == null) {
            schoolClass.setClassTeacher(null);
        }
        
        // Update assistant teacher if changed
        if (classDTO.getAssistantTeacherId() != null && 
            (schoolClass.getAssistantTeacher() == null || 
             !schoolClass.getAssistantTeacher().getId().equals(classDTO.getAssistantTeacherId()))) {
            User assistantTeacher = userRepository.findById(classDTO.getAssistantTeacherId())
                    .orElseThrow(() -> new RuntimeException("Assistant teacher not found with id: " + classDTO.getAssistantTeacherId()));
            schoolClass.setAssistantTeacher(assistantTeacher);
        } else if (classDTO.getAssistantTeacherId() == null) {
            schoolClass.setAssistantTeacher(null);
        }
        
        SchoolClass saved = classRepository.save(schoolClass);
        return convertToDTO(saved);
    }
    
    /**
     * Delete class (soft delete by setting inactive)
     */
    public void deleteClass(Long id) {
        SchoolClass schoolClass = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));
        
        // Check if class has students
        if (schoolClass.getCurrentStudentsCount() > 0) {
            throw new RuntimeException("Cannot delete class with students. Remove students first or set as inactive.");
        }
        
        classRepository.delete(schoolClass);
    }
    
    /**
     * Get classes for a teacher
     */
    public List<ClassDTO> getClassesForTeacher(Long teacherId) {
        return classRepository.findByTeacherInvolved(teacherId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get classes with available capacity
     */
    public List<ClassDTO> getClassesWithCapacity() {
        return classRepository.findClassesWithCapacity().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Search classes by name or code
     */
    public List<ClassDTO> searchClasses(String query) {
        List<SchoolClass> byName = classRepository.findByClassNameContainingIgnoreCase(query);
        List<SchoolClass> byCode = classRepository.findByClassCodeContainingIgnoreCase(query);
        
        // Combine and remove duplicates
        return byName.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get class statistics
     */
    public ClassStatisticsDTO getClassStatistics() {
        long totalClasses = classRepository.count();
        long activeClasses = classRepository.countByIsActiveTrue();
        long totalCapacity = classRepository.findAll().stream()
                .mapToInt(SchoolClass::getMaxStudents)
                .sum();
        long currentStudents = classRepository.findAll().stream()
                .mapToInt(SchoolClass::getCurrentStudentsCount)
                .sum();
        
        return ClassStatisticsDTO.builder()
                .totalClasses(totalClasses)
                .activeClasses(activeClasses)
                .inactiveClasses(totalClasses - activeClasses)
                .totalCapacity(totalCapacity)
                .currentStudents(currentStudents)
                .availableSlots(totalCapacity - currentStudents)
                .build();
    }
    
    /**
     * Increment student count (when student is added to class)
     */
    public void incrementStudentCount(Long classId) {
        SchoolClass schoolClass = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));
        
        if (!schoolClass.hasCapacity()) {
            throw new RuntimeException("Class " + schoolClass.getClassCode() + " is at full capacity");
        }
        
        schoolClass.addStudent();
        classRepository.save(schoolClass);
    }
    
    /**
     * Decrement student count (when student is removed from class)
     */
    public void decrementStudentCount(Long classId) {
        SchoolClass schoolClass = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));
        
        schoolClass.removeStudent();
        classRepository.save(schoolClass);
    }
    
    /**
     * Convert SchoolClass entity to ClassDTO
     */
    private ClassDTO convertToDTO(SchoolClass schoolClass) {
        return ClassDTO.builder()
                .id(schoolClass.getId())
                .classCode(schoolClass.getClassCode())
                .className(schoolClass.getClassName())
                .academicYear(schoolClass.getAcademicYear())
                .classTeacherId(schoolClass.getClassTeacher() != null ? schoolClass.getClassTeacher().getId() : null)
                .classTeacherName(schoolClass.getClassTeacher() != null ? schoolClass.getClassTeacher().getFullName() : null)
                .assistantTeacherId(schoolClass.getAssistantTeacher() != null ? schoolClass.getAssistantTeacher().getId() : null)
                .assistantTeacherName(schoolClass.getAssistantTeacher() != null ? schoolClass.getAssistantTeacher().getFullName() : null)
                .maxStudents(schoolClass.getMaxStudents())
                .currentStudentsCount(schoolClass.getCurrentStudentsCount())
                .classroomNumber(schoolClass.getClassroomNumber())
                .building(schoolClass.getBuilding())
                .isActive(schoolClass.getIsActive())
                .build();
    }
    
    /**
     * Statistics DTO
     */
    @Data
    @Builder
    public static class ClassStatisticsDTO {
        private long totalClasses;
        private long activeClasses;
        private long inactiveClasses;
        private long totalCapacity;
        private long currentStudents;
        private long availableSlots;
    }
}