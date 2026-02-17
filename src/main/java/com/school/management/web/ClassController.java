package com.school.management.web;

import com.school.management.dto.ClassDTO;
import com.school.management.service.ClassService;
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

@Controller
@RequestMapping("/classes")
public class ClassController {
    
    private final ClassService classService;
    private final TeacherService teacherService;
    private final UserService userService;
    
    public ClassController(ClassService classService, TeacherService teacherService, UserService userService) {
        this.classService = classService;
        this.teacherService = teacherService;
        this.userService = userService;
    }
    
    /**
     * List all classes
     */
    @GetMapping
    public String listClasses(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Boolean active,
            Model model) {
        
        List<ClassDTO> classes;
        
        if (search != null && !search.trim().isEmpty()) {
            classes = classService.searchClasses(search);
        } else {
            classes = classService.getAllClasses();
        }
        
        // Filter by year if specified
        if (year != null) {
            classes = classes.stream()
                    .filter(c -> year.equals(c.getAcademicYear()))
                    .toList();
        }
        
        // Filter by active status if specified
        if (active != null) {
            classes = classes.stream()
                    .filter(c -> active.equals(c.getIsActive()))
                    .toList();
        }
        
        model.addAttribute("classes", classes);
        model.addAttribute("search", search);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedActive", active);
        
        // Add statistics
        model.addAttribute("stats", classService.getClassStatistics());
        
        return "class/list";
    }
    
    /**
     * Show create class form
     */
    @GetMapping("/create")
    public String showCreateForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Long currentUserId = getCurrentUserId(userDetails);
        
        model.addAttribute("classDTO", new ClassDTO());
        model.addAttribute("teachers", teacherService.getAllTeachers());
        model.addAttribute("currentYear", java.time.Year.now().getValue());
        
        return "class/create";
    }
    
    /**
     * Create new class
     */
    @PostMapping("/create")
    public String createClass(
            @Valid @ModelAttribute("classDTO") ClassDTO classDTO,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("teachers", teacherService.getAllTeachers());
            model.addAttribute("currentYear", java.time.Year.now().getValue());
            return "class/create";
        }
        
        try {
            Long currentUserId = getCurrentUserId(userDetails);
            ClassDTO created = classService.createClass(classDTO, currentUserId);
            
            redirectAttributes.addFlashAttribute("success", 
                "Class '" + created.getClassName() + "' created successfully!");
            
            return "redirect:/classes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error creating class: " + e.getMessage());
            return "redirect:/classes/create";
        }
    }
    
    /**
     * Show class details
     */
    @GetMapping("/{id}")
    public String viewClass(@PathVariable Long id, Model model) {
        ClassDTO classDTO = classService.getClassById(id);
        
        if (classDTO == null) {
            return "redirect:/classes?error=Class+not+found";
        }
        
        model.addAttribute("classDTO", classDTO);
        
        // TODO: Add students in this class
        // model.addAttribute("students", studentService.getStudentsByClassId(id));
        
        return "class/view";
    }
    
    /**
     * Show edit class form
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        ClassDTO classDTO = classService.getClassById(id);
        
        if (classDTO == null) {
            return "redirect:/classes?error=Class+not+found";
        }
        
        model.addAttribute("classDTO", classDTO);
        model.addAttribute("teachers", teacherService.getAllTeachers());
        
        return "class/edit";
    }
    
    /**
     * Update class
     */
    @PostMapping("/{id}/edit")
    public String updateClass(
            @PathVariable Long id,
            @Valid @ModelAttribute("classDTO") ClassDTO classDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("teachers", teacherService.getAllTeachers());
            return "class/edit";
        }
        
        try {
            ClassDTO updated = classService.updateClass(id, classDTO);
            
            redirectAttributes.addFlashAttribute("success", 
                "Class '" + updated.getClassName() + "' updated successfully!");
            
            return "redirect:/classes/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error updating class: " + e.getMessage());
            return "redirect:/classes/" + id + "/edit";
        }
    }
    
    /**
     * Delete class
     */
    @PostMapping("/{id}/delete")
    public String deleteClass(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            classService.deleteClass(id);
            
            redirectAttributes.addFlashAttribute("success", 
                "Class deleted successfully!");
            
            return "redirect:/classes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error deleting class: " + e.getMessage());
            return "redirect:/classes/" + id;
        }
    }
    
    /**
     * Toggle class active status
     */
    @PostMapping("/{id}/toggle-active")
    public String toggleActive(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            ClassDTO classDTO = classService.getClassById(id);
            if (classDTO == null) {
                throw new RuntimeException("Class not found");
            }
            
            ClassDTO updateDTO = ClassDTO.builder()
                    .isActive(!classDTO.getIsActive())
                    .build();
            
            classService.updateClass(id, updateDTO);
            
            String status = updateDTO.getIsActive() ? "activated" : "deactivated";
            redirectAttributes.addFlashAttribute("success", 
                "Class " + status + " successfully!");
            
            return "redirect:/classes/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error toggling class status: " + e.getMessage());
            return "redirect:/classes/" + id;
        }
    }
    
    /**
     * Get classes for grade entry (AJAX)
     */
    @GetMapping("/api/for-grade-entry")
    @ResponseBody
    public List<ClassDTO> getClassesForGradeEntry() {
        return classService.getActiveClasses();
    }
    
    /**
     * Get classes with capacity (for student assignment)
     */
    @GetMapping("/api/with-capacity")
    @ResponseBody
    public List<ClassDTO> getClassesWithCapacity() {
        return classService.getClassesWithCapacity();
    }
    
    /**
     * Get current user ID from authentication
     */
    private Long getCurrentUserId(UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("User not authenticated");
        }
        
        String username = userDetails.getUsername();
        com.school.management.domain.User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        return user.getId();
    }
}