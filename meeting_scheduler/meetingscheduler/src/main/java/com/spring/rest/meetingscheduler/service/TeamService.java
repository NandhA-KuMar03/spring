package com.spring.rest.meetingscheduler.service;

import com.spring.rest.meetingscheduler.entity.Team;

import java.util.List;

public interface TeamService {

    List<Team> findAll();

    Team findById(int id);

    Team save(Team team);

    void deleteById(int id);

    Team addEmployee(List<Integer> employeeId, int teamId);

}
