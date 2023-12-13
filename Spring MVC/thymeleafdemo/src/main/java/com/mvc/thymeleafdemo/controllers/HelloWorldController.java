package com.mvc.thymeleafdemo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloWorldController {

    @RequestMapping("/showForm")
    public String showForm(){
        return "helloworld-form";
    }

    @GetMapping("/processForm")
    public String processForm(){
        return "helloWorld";
    }

    @GetMapping("/upperCase")
    public String upperCaseForm(){
        return "upper-case";
    }

    @GetMapping("/processFormUpperCase")
    public String convertUpper(HttpServletRequest request, Model model){
        String name = request.getParameter("StudentName");
        name = name.toUpperCase();
        model.addAttribute("message", name);
        return "result";
    }

    @GetMapping("/processFormV3")
    public String convertUpperV3(@RequestParam("StudentName") String name, Model model){
        name = name.toUpperCase();
        model.addAttribute("message", name);
        return "result";
    }

}
