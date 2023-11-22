package com.spring.rest.meetingscheduler.repository;

import com.spring.rest.meetingscheduler.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByEmployeeId(int id);
}
