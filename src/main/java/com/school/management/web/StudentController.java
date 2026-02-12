package com.school.management.web;

import com.school.management.domain.Student;
import com.school.management.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "student/list";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("student", new StudentService.StudentRegistrationDto());
        return "student/register";
    }

    @PostMapping("/register")
    public String registerStudent(@ModelAttribute StudentService.StudentRegistrationDto dto,
                                 RedirectAttributes redirectAttributes) {
        try {
            Student student = studentService.registerStudent(dto);
            redirectAttributes.addFlashAttribute("success", 
                "Student registered successfully! Display ID: " + student.getUser().getDisplayId());
            return "redirect:/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to register student: " + e.getMessage());
            return "redirect:/students/register";
        }
    }

    @GetMapping("/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        model.addAttribute("student", student);
        return "student/view";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        model.addAttribute("student", student);
        model.addAttribute("updateDto", new StudentService.StudentUpdateDto());
        return "student/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateStudent(@PathVariable Long id,
                               @ModelAttribute StudentService.StudentUpdateDto dto,
                               RedirectAttributes redirectAttributes) {
        try {
            studentService.updateStudent(id, dto);
            redirectAttributes.addFlashAttribute("success", "Student updated successfully");
            return "redirect:/students/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update student: " + e.getMessage());
            return "redirect:/students/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/graduate")
    public String graduateStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.graduateStudent(id);
            redirectAttributes.addFlashAttribute("success", "Student graduated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to graduate student: " + e.getMessage());
        }
        return "redirect:/students";
    }

    @GetMapping("/class/{className}")
    public String getStudentsByClass(@PathVariable String className, Model model) {
        List<Student> students = studentService.getStudentsByClass(className);
        model.addAttribute("students", students);
        model.addAttribute("className", className);
        return "student/list";
    }

    @GetMapping("/teacher/{teacherId}")
    public String getStudentsByTeacher(@PathVariable Long teacherId, Model model) {
        List<Student> students = studentService.getStudentsByTeacher(teacherId);
        model.addAttribute("students", students);
        return "student/list";
    }
}