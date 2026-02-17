package com.school.management.web;

import com.school.management.dto.DepartmentDTO;
import com.school.management.service.DepartmentService;
import com.school.management.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final UserService userService;

    public DepartmentController(DepartmentService departmentService, UserService userService) {
        this.departmentService = departmentService;
        this.userService = userService;
    }

    @GetMapping
    public String listDepartments(Model model) {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        return "department/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("departmentDTO", new DepartmentDTO());
        return "department/create";
    }

    @PostMapping("/create")
    public String createDepartment(
            @Valid @ModelAttribute DepartmentDTO departmentDTO,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "department/create";
        }
        
        try {
            Long currentUserId = getCurrentUserId(userDetails);
            departmentService.createDepartment(departmentDTO, currentUserId);
            redirectAttributes.addFlashAttribute("successMessage", "Department created successfully!");
            return "redirect:/departments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create department: " + e.getMessage());
            return "redirect:/departments/create";
        }
    }

    @GetMapping("/{id}")
    public String viewDepartment(@PathVariable Long id, Model model) {
        DepartmentDTO departmentDTO = departmentService.getDepartmentById(id);
        model.addAttribute("department", departmentDTO);
        return "department/view";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        DepartmentDTO departmentDTO = departmentService.getDepartmentById(id);
        model.addAttribute("departmentDTO", departmentDTO);
        return "department/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateDepartment(
            @PathVariable Long id,
            @Valid @ModelAttribute DepartmentDTO departmentDTO,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "department/edit";
        }
        
        try {
            Long currentUserId = getCurrentUserId(userDetails);
            departmentService.updateDepartment(id, departmentDTO, currentUserId);
            redirectAttributes.addFlashAttribute("successMessage", "Department updated successfully!");
            return "redirect:/departments";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update department: " + e.getMessage());
            return "redirect:/departments/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteDepartment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            Long currentUserId = getCurrentUserId(userDetails);
            departmentService.deleteDepartment(id, currentUserId);
            redirectAttributes.addFlashAttribute("successMessage", "Department deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete department: " + e.getMessage());
        }
        
        return "redirect:/departments";
    }

    @PostMapping("/{id}/toggle-active")
    public String toggleDepartmentActive(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            Long currentUserId = getCurrentUserId(userDetails);
            departmentService.toggleDepartmentActive(id, currentUserId);
            redirectAttributes.addFlashAttribute("successMessage", "Department status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update department status: " + e.getMessage());
        }
        
        return "redirect:/departments";
    }

    /**
     * Get current user ID from authentication
     */
    private Long getCurrentUserId(UserDetails userDetails) {
        if (userDetails == null) {
            // Return a default admin user ID or throw exception
            // For now, return 1 (assuming admin user with ID 1 exists)
            return 1L;
        }
        
        String username = userDetails.getUsername();
        com.school.management.domain.User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        return user.getId();
    }
}