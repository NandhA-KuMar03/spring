package com.spring.rest.meetingscheduler.service;

import com.spring.rest.meetingscheduler.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> findAll();

    Employee findById(int id);

    Employee save(Employee employee);

    void deleteById(int id);

}
