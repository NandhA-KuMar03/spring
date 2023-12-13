package com.spring.rest.meetingscheduler.dao;

import com.spring.rest.meetingscheduler.entity.Employee;

import java.util.List;

public interface EmployeeDAO {

    List<Employee> findAll();

    Employee findById(int id);

    Employee save(Employee employee);

    void deleteById(int id);

}
