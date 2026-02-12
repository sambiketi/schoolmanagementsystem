package com.school.management.web;

import com.school.management.domain.User;
import com.school.management.service.UserService;
import com.school.management.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    private final UserService userService;
    private final StudentService studentService;
    
    public AdminController(UserService userService, StudentService studentService) {
        this.userService = userService;
        this.studentService = studentService;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        String username = auth.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // REAL DATA FROM DATABASE
        model.addAttribute("user", user);
        model.addAttribute("studentCount", studentService.getAllStudents().size());
        model.addAttribute("teacherCount", userService.getUsersByType("TEACHER").size());
        model.addAttribute("adminCount", userService.getUsersByType("ADMIN").size());
        
        // Placeholder counts - will implement later
        model.addAttribute("departmentCount", 8);
        model.addAttribute("reportCount", 156);
        model.addAttribute("backupCount", 24);
        
        return "admin/dashboard";
    }
}