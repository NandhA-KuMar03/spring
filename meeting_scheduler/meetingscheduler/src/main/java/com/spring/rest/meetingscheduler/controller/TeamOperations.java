package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.Team;
import com.spring.rest.meetingscheduler.request.TeamRequestObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface TeamOperations {

    @GetMapping("/teams")
    List<Team> getAllTeams();

    @PostMapping("/teams")
    @ResponseStatus(HttpStatus.CREATED)
    Team createTeam(@RequestBody Team team);

    @GetMapping("/teams/{teamId}")
    Team findById(@PathVariable int teamId);

    @DeleteMapping("/teams/{teamId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTeam(@PathVariable int teamId);

    @PutMapping("/teams/employee")
    Team addEmployeeToTeam(@RequestBody TeamRequestObject teamRequestObject);
}
