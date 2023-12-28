package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmployeeOperations {

    @GetMapping("/employees")
    List<Employee> getAllEmployees();

//    @PostMapping(value = "/employees", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    @ResponseStatus(value = HttpStatus.CREATED)
//    Employee createEmployee(@RequestPart("employee") Employee employee, @RequestPart("file") MultipartFile file) throws IOException;
    @PostMapping("/employees")
    @ResponseStatus(value = HttpStatus.CREATED)
    Employee createEmployee(@RequestBody Employee employee);
    @GetMapping("/employees/{employeeId}")
    Employee findById(@PathVariable int employeeId);

    @DeleteMapping("/employees/{employeeId}")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void deleteEmployee(@PathVariable int employeeId);

}
