package com.school.management.web;

import com.school.management.dto.ClassDTO;
import com.school.management.dto.GradeDTO;
import com.school.management.dto.GradeEntryDTO;
import com.school.management.service.ClassService;
import com.school.management.service.GradeService;
import com.school.management.service.StudentService;
import com.school.management.service.TeacherService;
import com.school.management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/grades")
public class GradeController {

    private final GradeService gradeService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final ClassService classService;
    private final UserService userService;

    public GradeController(
            GradeService gradeService,
            TeacherService teacherService,
            StudentService studentService,
            ClassService classService,
            UserService userService) {
        this.gradeService = gradeService;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.classService = classService;
        this.userService = userService;
    }

    /**
     * Show grade entry form
     */
    @GetMapping("/entry")
    public String showGradeEntryForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // Get current teacher
        Long teacherId = getCurrentTeacherId(userDetails);
        
        model.addAttribute("gradeEntry", new GradeEntryDTO());
        
        // Get classes for the current teacher
        List<ClassDTO> teacherClasses = classService.getClassesForTeacher(teacherId);
        List<String> classNames = teacherClasses.stream()
                .map(ClassDTO::getClassName)
                .collect(Collectors.toList());
        
        model.addAttribute("classes", classNames);
        model.addAttribute("subjects", gradeService.getTeacherSubjects(teacherId));
        model.addAttribute("academicYears", gradeService.getAcademicYears());
        model.addAttribute("terms", gradeService.getTerms());
        
        return "grade/entry";
    }

    /**
     * Load students for a class (AJAX)
     */
    @GetMapping("/entry/students")
    @ResponseBody
    public List<Map<String, Object>> getStudentsForClass(@RequestParam String className) {
        return gradeService.getStudentsForGradeEntry(className);
    }

    /**
     * Process grade entry
     */
    @PostMapping("/entry")
    public String submitGrades(
            @Valid @ModelAttribute("gradeEntry") GradeEntryDTO gradeEntry,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            Long teacherId = getCurrentTeacherId(userDetails);
            
            // Get classes for the current teacher
            List<ClassDTO> teacherClasses = classService.getClassesForTeacher(teacherId);
            List<String> classNames = teacherClasses.stream()
                    .map(ClassDTO::getClassName)
                    .collect(Collectors.toList());
            
            model.addAttribute("classes", classNames);
            model.addAttribute("subjects", gradeService.getTeacherSubjects(teacherId));
            model.addAttribute("academicYears", gradeService.getAcademicYears());
            model.addAttribute("terms", gradeService.getTerms());
            return "grade/entry";
        }

        try {
            Long teacherId = getCurrentTeacherId(userDetails);
            List<GradeDTO> saved = gradeService.enterGrades(gradeEntry, teacherId);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                saved.size() + " grades entered successfully as DRAFT");
            
            return "redirect:/grades/my-grades";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error entering grades: " + e.getMessage());
            return "redirect:/grades/entry";
        }
    }

    /**
     * View grades entered by current teacher
     */
    @GetMapping("/my-grades")
    public String myGrades(
            @RequestParam(required = false) String status,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        
        Long teacherId = getCurrentTeacherId(userDetails);
        List<GradeDTO> grades = gradeService.getTeacherGrades(teacherId, status);
        
        model.addAttribute("grades", grades);
        model.addAttribute("status", status);
        model.addAttribute("draftCount", 
            grades.stream().filter(g -> "DRAFT".equals(g.getSubmissionStatus())).count());
        model.addAttribute("submittedCount", 
            grades.stream().filter(g -> "SUBMITTED".equals(g.getSubmissionStatus())).count());
        model.addAttribute("approvedCount", 
            grades.stream().filter(g -> "APPROVED".equals(g.getSubmissionStatus())).count());
        
        return "grade/list";
    }

    /**
     * Submit grades for review
     */
    @PostMapping("/submit")
    public String submitForReview(
            @RequestParam List<Long> gradeIds,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            Long teacherId = getCurrentTeacherId(userDetails);
            List<GradeDTO> submitted = gradeService.submitForReview(gradeIds, teacherId);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                submitted.size() + " grades submitted for review");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error submitting grades: " + e.getMessage());
        }
        
        return "redirect:/grades/my-grades";
    }

    /**
     * View pending reviews (for class teachers)
     */
    @GetMapping("/pending-review")
    public String pendingReviews(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        
        Long teacherId = getCurrentTeacherId(userDetails);
        List<GradeDTO> pending = gradeService.getPendingReviews(teacherId);
        
        model.addAttribute("grades", pending);
        model.addAttribute("isClassTeacher", true);
        
        return "grade/review";
    }

    /**
     * Review a grade
     */
    @PostMapping("/review/{gradeId}")
    public String reviewGrade(
            @PathVariable Long gradeId,
            @RequestParam String action,
            @RequestParam(required = false) String comments,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            Long classTeacherId = getCurrentTeacherId(userDetails);
            GradeDTO reviewed = gradeService.reviewGrade(gradeId, classTeacherId, action, comments);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Grade " + action.toLowerCase() + "d successfully");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error reviewing grade: " + e.getMessage());
        }
        
        return "redirect:/grades/pending-review";
    }

    /**
     * View student report card
     */
    @GetMapping("/student/{studentId}")
    public String studentGrades(
            @PathVariable Long studentId,
            @RequestParam(required = false) Integer academicYear,
            @RequestParam(required = false) Integer term,
            Model model) {
        
        List<GradeDTO> grades = gradeService.getStudentGrades(studentId, academicYear, term);
        var student = studentService.getStudentById(studentId);
        
        model.addAttribute("student", student);
        model.addAttribute("grades", grades);
        model.addAttribute("academicYears", gradeService.getAcademicYears());
        model.addAttribute("terms", gradeService.getTerms());
        
        return "grade/student-grades";
    }

    /**
     * Edit a grade
     */
    @GetMapping("/edit/{gradeId}")
    public String showEditForm(@PathVariable Long gradeId, Model model) {
        GradeDTO grade = gradeService.getTeacherGrades(null, null).stream()
            .filter(g -> g.getId().equals(gradeId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Grade not found"));
        
        model.addAttribute("grade", grade);
        return "grade/edit";
    }

    /**
     * Update a grade
     */
    @PostMapping("/edit/{gradeId}")
    public String updateGrade(
            @PathVariable Long gradeId,
            @RequestParam String gradeValue,
            @RequestParam(required = false) String comments,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            Long teacherId = getCurrentTeacherId(userDetails);
            GradeDTO updated = gradeService.updateGrade(gradeId, gradeValue, comments, teacherId);
            
            redirectAttributes.addFlashAttribute("successMessage", "Grade updated successfully");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error updating grade: " + e.getMessage());
        }
        
        return "redirect:/grades/my-grades";
    }

    /**
     * Delete a grade
     */
    @PostMapping("/delete/{gradeId}")
    public String deleteGrade(
            @PathVariable Long gradeId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            Long teacherId = getCurrentTeacherId(userDetails);
            gradeService.deleteGrade(gradeId, teacherId);
            
            redirectAttributes.addFlashAttribute("successMessage", "Grade deleted successfully");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error deleting grade: " + e.getMessage());
        }
        
        return "redirect:/grades/my-grades";
    }

    /**
     * Helper method to get current teacher ID
     */
    private Long getCurrentTeacherId(UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("User not authenticated");
        }
        
        String username = userDetails.getUsername();
        com.school.management.domain.User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        // Verify user is a teacher
        if (!"TEACHER".equals(user.getUserType()) && !"ADMIN".equals(user.getUserType())) {
            throw new RuntimeException("User is not authorized to enter grades. User type: " + user.getUserType());
        }
        
        return user.getId();
    }
}