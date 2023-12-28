package com.spring.rest.meetingscheduler.service;


import com.spring.rest.meetingscheduler.dao.EmployeeDAO;
import com.spring.rest.meetingscheduler.entity.Employee;
import com.spring.rest.meetingscheduler.entity.Team;
import com.spring.rest.meetingscheduler.serviceimpl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    EmployeeServiceImpl employeeService;

    @Mock
    EmployeeDAO employeeDAO;


    @Test
    void getEmployees(){
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee("nandha", "nandha@g.com");
        employee.setEmployeeId(1);
        employees.add(employee);
        when(employeeDAO.findAll()).thenReturn(employees);
        List<Employee> response = employeeService.findAll();
        assertEquals(response.get(0).getEmployeeId(), employees.get(0).getEmployeeId());
    }

    @Test
    void findById(){
        Employee employee = new Employee("nandha", "nandha@g.com");
        employee.setEmployeeId(1);
        when(employeeDAO.findById(1)).thenReturn(employee);
        Employee response = employeeService.findById(1);
        assertEquals(response,employee);
    }

    @Test
    void saveEmployee(){
        Employee employee = new Employee("nandha", "nandha@g.com");
        employee.setEmployeeId(1);
        when(employeeDAO.save(employee)).thenReturn(employee);
        Employee response = employeeService.save(employee);
        assertEquals(response,employee);
    }

}
