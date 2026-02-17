package com.school.management.web;

import com.school.management.dto.AdministratorDTO;
import com.school.management.service.AdministratorService;
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
@RequestMapping("/administrators")
public class AdministratorController {

    private final AdministratorService administratorService;
    private final DepartmentService departmentService;
    private final UserService userService;

    public AdministratorController(AdministratorService administratorService, 
                                  DepartmentService departmentService,
                                  UserService userService) {
        this.administratorService = administratorService;
        this.departmentService = departmentService;
        this.userService = userService;
    }

    @GetMapping
    public String listAdministrators(Model model) {
        List<AdministratorDTO> administrators = administratorService.getAllAdministrators();
        model.addAttribute("administrators", administrators);
        return "administrator/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("administratorDTO", new AdministratorDTO());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "administrator/create";
    }

    @PostMapping("/create")
    public String createAdministrator(
            @Valid @ModelAttribute AdministratorDTO administratorDTO,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "administrator/create";
        }
        
        try {
            Long currentUserId = getCurrentUserId(userDetails);
            administratorService.createAdministrator(administratorDTO, currentUserId);
            redirectAttributes.addFlashAttribute("successMessage", "Administrator created successfully!");
            return "redirect:/administrators";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create administrator: " + e.getMessage());
            return "redirect:/administrators/create";
        }
    }

    @GetMapping("/{id}")
    public String viewAdministrator(@PathVariable Long id, Model model) {
        AdministratorDTO administratorDTO = administratorService.getAdministratorById(id);
        model.addAttribute("administrator", administratorDTO);
        return "administrator/view";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        AdministratorDTO administratorDTO = administratorService.getAdministratorById(id);
        model.addAttribute("administratorDTO", administratorDTO);
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "administrator/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateAdministrator(
            @PathVariable Long id,
            @Valid @ModelAttribute AdministratorDTO administratorDTO,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "administrator/edit";
        }
        
        try {
            Long currentUserId = getCurrentUserId(userDetails);
            administratorService.updateAdministrator(id, administratorDTO, currentUserId);
            redirectAttributes.addFlashAttribute("successMessage", "Administrator updated successfully!");
            return "redirect:/administrators";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update administrator: " + e.getMessage());
            return "redirect:/administrators/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteAdministrator(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            Long currentUserId = getCurrentUserId(userDetails);
            administratorService.deleteAdministrator(id, currentUserId);
            redirectAttributes.addFlashAttribute("successMessage", "Administrator deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete administrator: " + e.getMessage());
        }
        
        return "redirect:/administrators";
    }

    @PostMapping("/{id}/toggle-active")
    public String toggleAdministratorActive(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            Long currentUserId = getCurrentUserId(userDetails);
            administratorService.toggleAdministratorActive(id, currentUserId);
            redirectAttributes.addFlashAttribute("successMessage", "Administrator status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update administrator status: " + e.getMessage());
        }
        
        return "redirect:/administrators";
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