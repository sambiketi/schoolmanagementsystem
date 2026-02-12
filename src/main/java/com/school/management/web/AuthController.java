package com.school.management.web;

import com.school.management.domain.User;
import com.school.management.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    
    private final UserService userService;
    
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("user", user);
        
        // Route to role-specific dashboard
        return switch (user.getUserType()) {
            case "ADMIN" -> "admin/dashboard";
            case "TEACHER" -> "teacher/dashboard";
            case "STUDENT" -> "student/dashboard";
            default -> "dashboard";
        };
    }
    
    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }
}