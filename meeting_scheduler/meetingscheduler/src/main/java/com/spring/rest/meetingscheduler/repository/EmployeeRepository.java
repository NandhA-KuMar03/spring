package com.spring.rest.meetingscheduler.repository;

import com.spring.rest.meetingscheduler.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
