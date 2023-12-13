package com.luv2code.springboot.mvccrud.controller;

import com.luv2code.springboot.mvccrud.entity.Employee;
import com.luv2code.springboot.mvccrud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/list")
    public String listEmployees(Model model){
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("employees", employees);
        return "employees-list";
    }

    @GetMapping("/employee")
    public String addEmployee(Model model){
        model.addAttribute("employee", new Employee());
        return "new-employee-form";
    }

    @PostMapping("/employee")
    public String submitEmployee(@ModelAttribute("employee")Employee employee ){
        employeeService.save(employee);
        return "redirect:/employees/list";
    }

    @GetMapping("/update")
    public String updateEmployeeForm(@RequestParam("employeeId")int id, Model model){
        Employee employee = employeeService.findById(id);
        model.addAttribute("employee", employee);
        return "new-employee-form";
    }

    @GetMapping("/delete")
    public String deleteEmployee(@RequestParam("employeeId")int id){
        employeeService.deleteById(id);
        return "redirect:/employees/list";
    }
}
