package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.Employee;
import com.spring.rest.meetingscheduler.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface EmployeeOperations {

    @GetMapping("/employees")
    List<Employee> getAllEmployees();

    @PostMapping("/employees")
    @ResponseStatus(value = HttpStatus.CREATED)
    Employee createEmployee(@RequestBody Employee employee);

    @GetMapping("/employees/{employeeId}")
    Employee findById(@PathVariable int employeeId);

    @DeleteMapping("/employees/{employeeId}")
    ResponseEntity<CommonResponse> deleteEmployee(@PathVariable int employeeId);

}
