package com.school.management.service;

import com.school.management.domain.SubjectGrade;
import com.school.management.domain.User;
import com.school.management.dto.GradeDTO;
import com.school.management.dto.GradeEntryDTO;
import com.school.management.repository.SubjectGradeRepository;
import com.school.management.repository.UserRepository;
import com.school.management.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class GradeService {

    private final SubjectGradeRepository gradeRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final UserService userService;

    public GradeService(
            SubjectGradeRepository gradeRepository,
            UserRepository userRepository,
            StudentRepository studentRepository,
            UserService userService) {
        this.gradeRepository = gradeRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.userService = userService;
    }

    /**
     * Enter grades for multiple students at once
     */
    public List<GradeDTO> enterGrades(GradeEntryDTO entryDTO, Long teacherId) {
        List<GradeDTO> savedGrades = new ArrayList<>();
        User teacher = userService.findById(teacherId);

        for (GradeEntryDTO.StudentGradeDTO studentGrade : entryDTO.getStudentGrades()) {
            // Check if grade already exists
            boolean exists = gradeRepository.existsByStudentAndSubjectAndTerm(
                studentGrade.getStudentId(),
                entryDTO.getSubjectName(),
                entryDTO.getAcademicYear(),
                entryDTO.getTermNumber()
            );

            if (exists) {
                throw new RuntimeException("Grade already exists for student ID: " + 
                    studentGrade.getStudentId() + " in " + entryDTO.getSubjectName() + 
                    " Term " + entryDTO.getTermNumber());
            }

            // Create new grade
            SubjectGrade grade = new SubjectGrade();
            grade.setStudent(userService.findById(studentGrade.getStudentId()));
            grade.setSubjectName(entryDTO.getSubjectName());
            grade.setTeacher(teacher);
            grade.setAcademicYear(entryDTO.getAcademicYear());
            grade.setTermNumber(entryDTO.getTermNumber());
            grade.setGradingDate(entryDTO.getGradingDate());
            grade.setGradeValue(studentGrade.getGradeValue());
            grade.setGradeType(entryDTO.getGradeType());
            grade.setMaxPossibleValue(entryDTO.getMaxPossibleValue());
            grade.setTeacherComments(studentGrade.getComments());
            grade.setSubmissionStatus("DRAFT");
            grade.setForwardedToClassTeacher(false);
            grade.setClassTeacherReviewed(false);
            grade.setCreatedBy(teacher);
            grade.setCreatedAt(LocalDateTime.now());

            // Get class teacher from student's class
            if (grade.getStudent().getStudent() != null && 
                grade.getStudent().getStudent().getClassTeacherId() != null) {
                grade.setClassTeacher(
                    userService.findById(grade.getStudent().getStudent().getClassTeacherId())
                );
            }

            SubjectGrade saved = gradeRepository.save(grade);
            savedGrades.add(convertToDTO(saved));
        }

        return savedGrades;
    }

    /**
     * Submit grades to class teacher for review
     */
    public List<GradeDTO> submitForReview(List<Long> gradeIds, Long teacherId) {
        List<GradeDTO> submittedGrades = new ArrayList<>();
        
        for (Long gradeId : gradeIds) {
            SubjectGrade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + gradeId));

            // Verify this teacher owns these grades
            if (!grade.getTeacher().getId().equals(teacherId)) {
                throw new RuntimeException("You can only submit your own grades");
            }

            grade.setSubmissionStatus("SUBMITTED");
            grade.setForwardedToClassTeacher(true);
            
            SubjectGrade saved = gradeRepository.save(grade);
            submittedGrades.add(convertToDTO(saved));
        }

        return submittedGrades;
    }

    /**
     * Review grades as class teacher
     */
    public GradeDTO reviewGrade(Long gradeId, Long classTeacherId, String status, String comments) {
        SubjectGrade grade = gradeRepository.findById(gradeId)
            .orElseThrow(() -> new RuntimeException("Grade not found with id: " + gradeId));

        // Verify this class teacher is responsible
        if (grade.getClassTeacher() == null || 
            !grade.getClassTeacher().getId().equals(classTeacherId)) {
            throw new RuntimeException("You are not the class teacher for this student");
        }

        grade.setClassTeacherReviewed(true);
        grade.setClassTeacherComments(comments);

        if ("APPROVE".equalsIgnoreCase(status)) {
            grade.setSubmissionStatus("APPROVED");
        } else if ("REJECT".equalsIgnoreCase(status)) {
            grade.setSubmissionStatus("REJECTED");
            grade.setForwardedToClassTeacher(false); // Send back to teacher
        }

        SubjectGrade saved = gradeRepository.save(grade);
        return convertToDTO(saved);
    }

    /**
     * Admin approval of grades
     */
    public GradeDTO adminApproveGrade(Long gradeId, Long adminId) {
        SubjectGrade grade = gradeRepository.findById(gradeId)
            .orElseThrow(() -> new RuntimeException("Grade not found with id: " + gradeId));

        grade.setSubmissionStatus("APPROVED");
        
        SubjectGrade saved = gradeRepository.save(grade);
        return convertToDTO(saved);
    }

    /**
     * Get grades for a student
     */
    public List<GradeDTO> getStudentGrades(Long studentId, Integer academicYear, Integer termNumber) {
        List<SubjectGrade> grades;
        
        if (academicYear != null && termNumber != null) {
            grades = gradeRepository.findByStudentIdAndAcademicYearAndTermNumber(
                studentId, academicYear, termNumber);
        } else {
            grades = gradeRepository.findByStudentId(studentId);
        }

        return grades.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get grades entered by a teacher
     */
    public List<GradeDTO> getTeacherGrades(Long teacherId, String status) {
        List<SubjectGrade> grades = gradeRepository.findByTeacherId(teacherId);
        
        if (status != null && !status.isEmpty()) {
            grades = grades.stream()
                .filter(g -> status.equals(g.getSubmissionStatus()))
                .collect(Collectors.toList());
        }

        return grades.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get grades pending review for a class teacher
     */
    public List<GradeDTO> getPendingReviews(Long classTeacherId) {
        return gradeRepository.findPendingReview(classTeacherId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Update a grade
     */
    public GradeDTO updateGrade(Long gradeId, String gradeValue, String comments, Long teacherId) {
        SubjectGrade grade = gradeRepository.findById(gradeId)
            .orElseThrow(() -> new RuntimeException("Grade not found with id: " + gradeId));

        // Only the original teacher or admin can update
        if (!grade.getTeacher().getId().equals(teacherId)) {
            // Check if user is admin
            User user = userService.findById(teacherId);
            if (!"ADMIN".equals(user.getUserType())) {
                throw new RuntimeException("You can only update your own grades");
            }
        }

        // Can only update if still in DRAFT or REJECTED
        if (!"DRAFT".equals(grade.getSubmissionStatus()) && 
            !"REJECTED".equals(grade.getSubmissionStatus())) {
            throw new RuntimeException("Cannot update grades that are already " + 
                grade.getSubmissionStatus());
        }

        grade.setGradeValue(gradeValue);
        grade.setTeacherComments(comments);
        grade.setUpdatedBy(userService.findById(teacherId));
        grade.setUpdatedAt(LocalDateTime.now());

        // Reset status if it was rejected
        if ("REJECTED".equals(grade.getSubmissionStatus())) {
            grade.setSubmissionStatus("DRAFT");
            grade.setForwardedToClassTeacher(false);
        }

        SubjectGrade saved = gradeRepository.save(grade);
        return convertToDTO(saved);
    }

    /**
     * Delete a draft grade
     */
    public void deleteGrade(Long gradeId, Long teacherId) {
        SubjectGrade grade = gradeRepository.findById(gradeId)
            .orElseThrow(() -> new RuntimeException("Grade not found with id: " + gradeId));

        // Only delete if in DRAFT
        if (!"DRAFT".equals(grade.getSubmissionStatus())) {
            throw new RuntimeException("Cannot delete grades that are not in DRAFT status");
        }

        // Verify ownership
        if (!grade.getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("You can only delete your own grades");
        }

        gradeRepository.delete(grade);
    }

    /**
     * Get grade statistics for a class
     */
    public Map<String, Object> getClassGradeStatistics(String className, String subjectName, 
                                                      Integer academicYear, Integer termNumber) {
        Map<String, Object> stats = new HashMap<>();
        
        // Get all students in this class (you'll need a Class entity for this)
        // For now, we'll use a placeholder
        List<User> students = new ArrayList<>(); // Replace with actual class students
        
        List<SubjectGrade> grades = gradeRepository.findBySubjectAndTerm(
            subjectName, academicYear, termNumber);
        
        // Calculate statistics
        double sum = 0;
        int count = 0;
        int highest = 0;
        int lowest = 100;
        
        for (SubjectGrade grade : grades) {
            try {
                int value = Integer.parseInt(grade.getGradeValue());
                sum += value;
                count++;
                if (value > highest) highest = value;
                if (value < lowest) lowest = value;
            } catch (NumberFormatException e) {
                // Handle letter grades differently
            }
        }
        
        stats.put("average", count > 0 ? sum / count : 0);
        stats.put("highest", highest);
        stats.put("lowest", lowest);
        stats.put("totalStudents", students.size());
        stats.put("gradesEntered", count);
        stats.put("pendingGrades", students.size() - count);
        
        return stats;
    }

    /**
     * Convert SubjectGrade entity to GradeDTO
     */
    private GradeDTO convertToDTO(SubjectGrade grade) {
        GradeDTO dto = new GradeDTO();
        
        dto.setId(grade.getId());
        
        // Student info
        if (grade.getStudent() != null) {
            dto.setStudentId(grade.getStudent().getId());
            dto.setStudentName(grade.getStudent().getFullName());
            dto.setStudentDisplayId(grade.getStudent().getDisplayId());
        }
        
        dto.setSubjectName(grade.getSubjectName());
        
        // Teacher info
        if (grade.getTeacher() != null) {
            dto.setTeacherId(grade.getTeacher().getId());
            dto.setTeacherName(grade.getTeacher().getFullName());
        }
        
        // Class teacher info
        if (grade.getClassTeacher() != null) {
            dto.setClassTeacherId(grade.getClassTeacher().getId());
            dto.setClassTeacherName(grade.getClassTeacher().getFullName());
        }
        
        dto.setAcademicYear(grade.getAcademicYear());
        dto.setTermNumber(grade.getTermNumber());
        dto.setGradingDate(grade.getGradingDate());
        dto.setGradeValue(grade.getGradeValue());
        dto.setGradeType(grade.getGradeType());
        dto.setMaxPossibleValue(grade.getMaxPossibleValue());
        dto.setClassRank(grade.getClassRank());
        dto.setTotalInClass(grade.getTotalInClass());
        dto.setTeacherComments(grade.getTeacherComments());
        dto.setClassTeacherComments(grade.getClassTeacherComments());
        dto.setSubmissionStatus(grade.getSubmissionStatus());
        dto.setForwardedToClassTeacher(grade.getForwardedToClassTeacher());
        dto.setClassTeacherReviewed(grade.getClassTeacherReviewed());
        dto.setCreatedAt(grade.getCreatedAt());
        dto.setUpdatedAt(grade.getUpdatedAt());
        
        return dto;
    }

    /**
     * Get students in a class for grade entry
     */
    public List<Map<String, Object>> getStudentsForGradeEntry(String className) {
        // This would normally query students by class
        // For now, we'll return all active students
        return studentRepository.findAll().stream()
            .filter(s -> s.getUser().getIsActive())
            .map(student -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", student.getUser().getId());
                map.put("name", student.getUser().getFullName());
                map.put("displayId", student.getUser().getDisplayId());
                map.put("currentClass", student.getCurrentClass());
                return map;
            })
            .collect(Collectors.toList());
    }

    /**
     * Get subjects for a teacher
     */
    public List<String> getTeacherSubjects(Long teacherId) {
        User teacher = userService.findById(teacherId);
        if (teacher.getTeacher() != null && teacher.getTeacher().getSubjectsCertified() != null) {
            // Parse JSON subjects
            String subjectsJson = teacher.getTeacher().getSubjectsCertified();
            // Simple parsing - in reality, use ObjectMapper
            return List.of(subjectsJson.replace("[", "").replace("]", "").replace("\"", "").split(","));
        }
        return new ArrayList<>();
    }

    /**
     * Get available classes for grade entry
     * @Deprecated - Use ClassService.getClassesForTeacher() instead
     */
    @Deprecated
    public List<String> getAvailableClasses() {
        // Use ClassService to get real classes
        // For now, return hardcoded list as fallback
        // TODO: Replace with real class data once ClassService is injected
        return List.of(
            "Grade 1", "Grade 2", "Grade 3", "Grade 4", "Grade 5",
            "Grade 6", "Grade 7", "Grade 8", "Grade 9", "Grade 10",
            "Grade 11", "Grade 12"
        );
    }

    /**
     * Get academic years
     */
    public List<Integer> getAcademicYears() {
        int currentYear = LocalDate.now().getYear();
        return List.of(currentYear - 1, currentYear, currentYear + 1);
    }

    /**
     * Get terms
     */
    public List<Integer> getTerms() {
        return List.of(1, 2, 3);
    }
}