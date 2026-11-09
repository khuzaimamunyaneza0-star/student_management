package org.example.studentmanagement.controller;

import jakarta.validation.Valid;
import org.example.studentmanagement.model.Student;
import org.example.studentmanagement.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }

    @PostMapping("/register")
    public String registerStudent(@Valid @ModelAttribute("student") Student student,
                                  BindingResult result,
                                  Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        try {
            studentService.registerStudent(student);
            model.addAttribute("success", "Student registered successfully!");
            model.addAttribute("student", new Student());
        } catch (RuntimeException e) {
            e.printStackTrace();
            String reason = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            model.addAttribute("error", "Could not save: " + reason);
        }
        return "register";
    }

    @GetMapping("/all")
    public String showAllStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }
}