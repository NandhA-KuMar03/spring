package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.Employee;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface EmployeeOperations {

    @GetMapping("/employees")
    List<Employee> getAllEmployees();

    @PostMapping("/employees")
    Employee createEmployee(@RequestBody Employee employee);

    @GetMapping("/employees/{employeeId}")
    Employee findById(@PathVariable int employeeId);

    @DeleteMapping("/employees/{employeeId}")
    String deleteEmployee(@PathVariable int employeeId);

}
