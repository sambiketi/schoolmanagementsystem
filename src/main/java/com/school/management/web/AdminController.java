package com.school.management.web;

import com.school.management.service.AdministratorService;
import com.school.management.service.ClassService;
import com.school.management.service.DepartmentService;
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
    private final ClassService classService;
    private final DepartmentService departmentService;
    private final AdministratorService administratorService;

    public AdminController(StudentService studentService, TeacherService teacherService, 
                          ClassService classService, DepartmentService departmentService,
                          AdministratorService administratorService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.classService = classService;
        this.departmentService = departmentService;
        this.administratorService = administratorService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("studentCount", studentService.getAllStudents().size());
        model.addAttribute("teacherCount", teacherService.getAllTeachers().size());
        
        // Get class statistics
        ClassService.ClassStatisticsDTO classStats = classService.getClassStatistics();
        model.addAttribute("classCount", classStats.getActiveClasses());
        
        // Get department count
        model.addAttribute("departmentCount", departmentService.getAllDepartments().size());
        
        // Get administrator count
        model.addAttribute("administratorCount", administratorService.getAllAdministrators().size());
        
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
        } else {
            model.addAttribute("username", "Admin");
        }
        
        return "admin/dashboard";
    }
}