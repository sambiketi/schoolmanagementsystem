package com.school.management.repository;

import com.school.management.domain.SubjectGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectGradeRepository extends JpaRepository<SubjectGrade, Long> {
    
    // Find grades by student
    List<SubjectGrade> findByStudentId(Long studentId);
    
    // Find grades by teacher
    List<SubjectGrade> findByTeacherId(Long teacherId);
    
    // Find grades by class teacher (for review)
    List<SubjectGrade> findByClassTeacherId(Long classTeacherId);
    
    // Find grades by status (DRAFT, SUBMITTED, etc.)
    List<SubjectGrade> findBySubmissionStatus(String status);
    
    // Find grades for a specific student in a specific term
    List<SubjectGrade> findByStudentIdAndAcademicYearAndTermNumber(
        Long studentId, Integer academicYear, Integer termNumber);
    
    // Find grades for a specific subject in a class
    @Query("SELECT g FROM SubjectGrade g WHERE g.subjectName = :subjectName " +
           "AND g.academicYear = :academicYear AND g.termNumber = :termNumber")
    List<SubjectGrade> findBySubjectAndTerm(
        @Param("subjectName") String subjectName,
        @Param("academicYear") Integer academicYear,
        @Param("termNumber") Integer termNumber);
    
    // Check if grade exists for student/subject/term
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM SubjectGrade g " +
           "WHERE g.student.id = :studentId AND g.subjectName = :subjectName " +
           "AND g.academicYear = :academicYear AND g.termNumber = :termNumber")
    boolean existsByStudentAndSubjectAndTerm(
        @Param("studentId") Long studentId,
        @Param("subjectName") String subjectName,
        @Param("academicYear") Integer academicYear,
        @Param("termNumber") Integer termNumber);
    
    // Find grades pending class teacher review
    @Query("SELECT g FROM SubjectGrade g WHERE g.forwardedToClassTeacher = true " +
           "AND g.classTeacherReviewed = false AND g.classTeacher.id = :classTeacherId")
    List<SubjectGrade> findPendingReview(@Param("classTeacherId") Long classTeacherId);
    
    // Get average grade for a student in a term
    @Query("SELECT AVG(CAST(g.gradeValue AS double)) FROM SubjectGrade g " +
           "WHERE g.student.id = :studentId AND g.academicYear = :academicYear " +
           "AND g.termNumber = :termNumber AND g.submissionStatus = 'APPROVED'")
    Double getAverageGradeForStudent(
        @Param("studentId") Long studentId,
        @Param("academicYear") Integer academicYear,
        @Param("termNumber") Integer termNumber);
    
    // Get all grades submitted by a teacher that need approval
    @Query("SELECT g FROM SubjectGrade g WHERE g.teacher.id = :teacherId " +
           "AND g.submissionStatus = 'SUBMITTED'")
    List<SubjectGrade> findSubmittedByTeacher(@Param("teacherId") Long teacherId);
}