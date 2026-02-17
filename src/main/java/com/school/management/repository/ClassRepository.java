package com.school.management.repository;

import com.school.management.domain.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<SchoolClass, Long> {
    
    // Find by class code
    Optional<SchoolClass> findByClassCode(String classCode);
    
    // Find by academic year
    List<SchoolClass> findByAcademicYear(Integer academicYear);
    
    // Find by class teacher
    List<SchoolClass> findByClassTeacherId(Long teacherId);
    
    // Find by assistant teacher
    List<SchoolClass> findByAssistantTeacherId(Long teacherId);
    
    // Find active classes
    List<SchoolClass> findByIsActiveTrue();
    
    // Find classes with available capacity
    @Query("SELECT c FROM SchoolClass c WHERE c.isActive = true AND c.currentStudentsCount < c.maxStudents")
    List<SchoolClass> findClassesWithCapacity();
    
    // Find classes for a specific teacher (either as class teacher or assistant)
    @Query("SELECT c FROM SchoolClass c WHERE c.isActive = true AND " +
           "(c.classTeacher.id = :teacherId OR c.assistantTeacher.id = :teacherId)")
    List<SchoolClass> findByTeacherInvolved(@Param("teacherId") Long teacherId);
    
    // Find classes by name containing (for search)
    List<SchoolClass> findByClassNameContainingIgnoreCase(String className);
    
    // Find classes by code containing (for search)
    List<SchoolClass> findByClassCodeContainingIgnoreCase(String classCode);
    
    // Count active classes
    long countByIsActiveTrue();
    
    // Count classes by academic year
    long countByAcademicYear(Integer academicYear);
}