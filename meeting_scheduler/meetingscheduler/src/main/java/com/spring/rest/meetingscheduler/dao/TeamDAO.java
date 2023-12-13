package com.spring.rest.meetingscheduler.dao;

import com.spring.rest.meetingscheduler.entity.Team;

import java.util.List;

public interface TeamDAO {

    List<Team> findAll();

    Team findById(int id);

    Team save(Team team);

    void deleteById(int id);

    String addEmployee(int employeeId, int teamId);

}
