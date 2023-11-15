package com.spring.rest.restproject.dao;

import com.spring.rest.restproject.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
