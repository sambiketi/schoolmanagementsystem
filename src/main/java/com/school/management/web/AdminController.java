package com.school.management.web;

import com.school.management.service.StudentService;
import com.school.management.service.TeacherService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final StudentService studentService;
    private final TeacherService teacherService;

    public AdminController(StudentService studentService, TeacherService teacherService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("studentCount", studentService.getAllStudents().size());
        model.addAttribute("teacherCount", teacherService.getAllTeachers().size());
        
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
        } else {
            model.addAttribute("username", "Admin");
        }
        
        return "admin/dashboard";
    }
}