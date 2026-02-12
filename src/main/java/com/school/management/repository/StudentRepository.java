package com.school.management.repository;

import com.school.management.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    List<Student> findByCurrentClass(String currentClass);
    
    List<Student> findByStudentStatus(String status);
    
    @Query("SELECT s FROM Student s WHERE s.admissionYear = :year")
    List<Student> findByAdmissionYear(@Param("year") Integer year);
    
    @Query("SELECT s FROM Student s WHERE s.classTeacherId = :teacherId")
    List<Student> findByClassTeacherId(@Param("teacherId") Long teacherId);
}