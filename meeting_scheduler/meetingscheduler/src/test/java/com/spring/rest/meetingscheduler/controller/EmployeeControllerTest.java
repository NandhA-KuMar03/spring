package com.spring.rest.meetingscheduler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.rest.meetingscheduler.entity.Employee;
import com.spring.rest.meetingscheduler.entity.Team;
import com.spring.rest.meetingscheduler.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EmployeeControllerTest {

    @MockBean
    EmployeeService employeeService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllEmployees() throws Exception {
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee("nandha", "nandha@g.com");
        employee.setEmployeeId(1);
        employees.add(employee);
        when(employeeService.findAll()).thenReturn(employees);
        mockMvc.perform(get("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void createEmployee() throws Exception{
        Employee employee = new Employee("nandha", "nandha@g.com");
        employee.setEmployeeId(1);
        when(employeeService.save(employee)).thenReturn(employee);
        mockMvc.perform(post("/api/employees")
                        .content(new ObjectMapper().writeValueAsString(employee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void findByIdEmployee() throws Exception{
        Employee employee = new Employee("nandha", "nandha@g.com");
        employee.setEmployeeId(1);
        when(employeeService.findById(1)).thenReturn(employee);
        mockMvc.perform(get("/api/employees")
                        .param("employeeId", String.valueOf(1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
