package com.spring.rest.meetingscheduler.serviceimpl;

import com.spring.rest.meetingscheduler.dao.EmployeeDAO;
import com.spring.rest.meetingscheduler.entity.Employee;
import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import com.spring.rest.meetingscheduler.repository.MeetingDetailRepository;
import com.spring.rest.meetingscheduler.service.EmployeeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeDAO employeeDAO;
    private MeetingDetailRepository meetingDetailRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeDAO employeeDAO, MeetingDetailRepository meetingDetailRepository) {
        this.employeeDAO = employeeDAO;
        this.meetingDetailRepository = meetingDetailRepository;
    }

    @Override
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }

    @Override
    public Employee findById(int id) {
        return employeeDAO.findById(id);
    }

    @Transactional
    @Override
    public Employee save(Employee employee) {
        return employeeDAO.save(employee);
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        long n = meetingDetailRepository.deleteByEmployeeEmployeeId(id);
        employeeDAO.deleteById(id);
    }
}
