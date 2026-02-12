package com.school.management.repository;

import com.school.management.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    Optional<Teacher> findByEmployeeNumber(String employeeNumber);
    
    List<Teacher> findByEmploymentStatus(String status);
    
    List<Teacher> findByEmploymentType(String employmentType);
    
    @Query("SELECT t FROM Teacher t WHERE t.employmentStatus = 'ACTIVE'")
    List<Teacher> findAllActiveTeachers();
}