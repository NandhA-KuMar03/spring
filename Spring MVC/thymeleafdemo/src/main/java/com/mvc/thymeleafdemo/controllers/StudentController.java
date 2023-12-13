package com.mvc.thymeleafdemo.controllers;

import com.mvc.thymeleafdemo.model.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class StudentController {

    @Value("${countries}")
    private List countries;

    @Value("${codingLanguages}")
    private List languages;

    @Value("${OS}")
    private List OS;

    @GetMapping("/form")
    public String showForm(Model model){
        model.addAttribute("student", new Student());
        model.addAttribute("countries", countries);
        model.addAttribute("languages", languages);
        model.addAttribute("OS", OS);
        return "studentForm";
    }

    @PostMapping("/form")
    public String processForm(@ModelAttribute("student") Student student){
        return "confirmation";
    }

}
