package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.Employee;
import com.spring.rest.meetingscheduler.entity.Team;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TeamOperations {

    @GetMapping("/teams")
    List<Team> getAllTeams();

    @PostMapping("/teams")
    Team createTeam(@RequestBody Team team);

    @GetMapping("/teams/{teamId}")
    Team findById(@PathVariable int teamId);

    @DeleteMapping("/teams/{teamId}")
    String deleteTeam(@PathVariable int teamId);

    @GetMapping("/teams/employee")
    String addEmployeeToTeam(@RequestParam int employeeId, @RequestParam int teamID);
}
