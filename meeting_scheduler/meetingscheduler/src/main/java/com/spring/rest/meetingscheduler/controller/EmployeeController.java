package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.Employee;
import com.spring.rest.meetingscheduler.response.CommonResponse;
import com.spring.rest.meetingscheduler.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class EmployeeController implements EmployeeOperations{

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @Override
    public Employee createEmployee(Employee employee) {
        employee.setEmployeeId(0);
        Employee employee1 = employeeService.save(employee);
        return employee1;
    }

    @Override
    public Employee findById(int employeeId) {
        Employee employee = employeeService.findById(employeeId);
        if(employee==null)
            throw new RuntimeException("No such employee");
        return employee;
    }

    @Override
    public ResponseEntity<CommonResponse> deleteEmployee(int employeeId) {
        Employee employee = employeeService.findById(employeeId);
        if(employee == null)
            throw new RuntimeException("No such employee found");
        employeeService.deleteById(employeeId);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setStatusCode(HttpStatus.OK.value());
        commonResponse.setStatusMessage("Deleted");
        ResponseEntity<CommonResponse> response = new ResponseEntity<>(commonResponse, HttpStatus.OK);
        return response;
    }
}
