package com.school.management.web;

import com.school.management.dto.TeacherDTO;
import com.school.management.dto.TeacherRegistrationDTO;
import com.school.management.service.TeacherService;
import com.school.management.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final DepartmentService departmentService;

    public TeacherController(TeacherService teacherService, DepartmentService departmentService) {
        this.teacherService = teacherService;
        this.departmentService = departmentService;
    }

    /**
     * Show teacher dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("activeTeachers", teacherService.getAllActiveTeachers().size());
        model.addAttribute("totalTeachers", teacherService.getAllTeachers().size());
        model.addAttribute("recentTeachers", 
            teacherService.getAllTeachers().stream()
                .limit(5)
                .toList());
        return "teacher/dashboard";
    }

    /**
     * List all teachers
     */
    @GetMapping
    public String listTeachers(Model model) {
        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        model.addAttribute("teachers", teachers);
        model.addAttribute("activeCount", 
            teachers.stream().filter(t -> "ACTIVE".equals(t.getEmploymentStatus())).count());
        return "teacher/list";
    }

    /**
     * Show registration form
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("teacher", new TeacherRegistrationDTO());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("employmentTypes", List.of("PERMANENT", "CONTRACT", "PART_TIME", "VISITING"));
        return "teacher/register";
    }

    /**
     * Process registration form
     */
    @PostMapping("/register")
    public String registerTeacher(
            @Valid @ModelAttribute("teacher") TeacherRegistrationDTO registrationDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("employmentTypes", List.of("PERMANENT", "CONTRACT", "PART_TIME", "VISITING"));
            return "teacher/register";
        }

        try {
            TeacherDTO created = teacherService.registerTeacher(registrationDTO);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Teacher " + created.getFullName() + " registered successfully!");
            return "redirect:/teachers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error registering teacher: " + e.getMessage());
            return "redirect:/teachers/register";
        }
    }

    /**
     * View teacher details
     */
    @GetMapping("/{id}")
    public String viewTeacher(@PathVariable Long id, Model model) {
        try {
            TeacherDTO teacher = teacherService.getTeacherById(id);
            model.addAttribute("teacher", teacher);
            return "teacher/profile";
        } catch (Exception e) {
            return "redirect:/teachers?error=Teacher+not+found";
        }
    }

    /**
     * Show edit form
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            TeacherDTO teacher = teacherService.getTeacherById(id);
            
            // Convert TeacherDTO to RegistrationDTO for form binding
            TeacherRegistrationDTO formData = new TeacherRegistrationDTO();
            formData.setFullName(teacher.getFullName());
            formData.setEmail(teacher.getEmail());
            formData.setPhoneNumber(teacher.getPhoneNumber());
            formData.setGender(teacher.getGender());
            formData.setDateOfBirth(teacher.getDateOfBirth());
            formData.setAddress(teacher.getAddress());
            formData.setEmployeeNumber(teacher.getEmployeeNumber());
            formData.setHireDate(teacher.getHireDate());
            formData.setEmploymentType(teacher.getEmploymentType());
            formData.setContractEndDate(teacher.getContractEndDate());
            formData.setSalaryGrade(teacher.getSalaryGrade());
            formData.setQualificationSummary(teacher.getQualificationSummary());
            formData.setSubjectsCertified(teacher.getSubjectsCertified());
            formData.setDepartmentAffiliations(teacher.getDepartmentAffiliations());
            formData.setOfficialEmail(teacher.getOfficialEmail());
            formData.setOfficeExtension(teacher.getOfficeExtension());
            formData.setOfficeRoom(teacher.getOfficeRoom());
            formData.setTeachingHoursPerWeek(teacher.getTeachingHoursPerWeek());
            formData.setAdministrativeHoursPerWeek(teacher.getAdministrativeHoursPerWeek());
            
            // Username and password are not editable via this form
            formData.setUsername(teacher.getEmail()); // Placeholder
            
            model.addAttribute("teacher", formData);
            model.addAttribute("teacherId", id);
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("employmentTypes", List.of("PERMANENT", "CONTRACT", "PART_TIME", "VISITING"));
            
            return "teacher/edit";
        } catch (Exception e) {
            return "redirect:/teachers?error=Teacher+not+found";
        }
    }

    /**
     * Process edit form
     */
    @PostMapping("/{id}/edit")
    public String updateTeacher(
            @PathVariable Long id,
            @Valid @ModelAttribute("teacher") TeacherRegistrationDTO updateDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("teacherId", id);
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("employmentTypes", List.of("PERMANENT", "CONTRACT", "PART_TIME", "VISITING"));
            return "teacher/edit";
        }

        try {
            TeacherDTO updated = teacherService.updateTeacher(id, updateDTO);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Teacher " + updated.getFullName() + " updated successfully!");
            return "redirect:/teachers/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error updating teacher: " + e.getMessage());
            return "redirect:/teachers/" + id + "/edit";
        }
    }

    /**
     * Update teacher status
     */
    @PostMapping("/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {
        
        try {
            teacherService.updateTeacherStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Teacher status updated to " + status);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error updating status: " + e.getMessage());
        }
        
        return "redirect:/teachers/" + id;
    }

    /**
     * Deactivate teacher
     */
    @PostMapping("/{id}/deactivate")
    public String deactivateTeacher(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            teacherService.deactivateTeacher(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Teacher deactivated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error deactivating teacher: " + e.getMessage());
        }
        
        return "redirect:/teachers";
    }

    /**
     * Filter teachers by department
     */
    @GetMapping("/filter/department/{departmentId}")
    public String filterByDepartment(
            @PathVariable Long departmentId,
            Model model) {
        
        List<TeacherDTO> teachers = teacherService.getTeachersByDepartment(departmentId);
        model.addAttribute("teachers", teachers);
        model.addAttribute("filterType", "Department");
        model.addAttribute("filterValue", departmentService.getDepartmentById(departmentId).getName());
        return "teacher/list";
    }

    /**
     * Filter teachers by subject
     */
    @GetMapping("/filter/subject")
    public String filterBySubject(
            @RequestParam String subject,
            Model model) {
        
        List<TeacherDTO> teachers = teacherService.getTeachersBySubject(subject);
        model.addAttribute("teachers", teachers);
        model.addAttribute("filterType", "Subject");
        model.addAttribute("filterValue", subject);
        return "teacher/list";
    }

    /**
     * Search teachers
     */
    @GetMapping("/search")
    public String searchTeachers(
            @RequestParam String keyword,
            Model model) {
        
        List<TeacherDTO> allTeachers = teacherService.getAllTeachers();
        List<TeacherDTO> filtered = allTeachers.stream()
            .filter(t -> t.getFullName().toLowerCase().contains(keyword.toLowerCase()) ||
                         t.getEmployeeNumber().toLowerCase().contains(keyword.toLowerCase()) ||
                         (t.getEmail() != null && t.getEmail().toLowerCase().contains(keyword.toLowerCase())))
            .toList();
        
        model.addAttribute("teachers", filtered);
        model.addAttribute("searchKeyword", keyword);
        return "teacher/list";
    }
}