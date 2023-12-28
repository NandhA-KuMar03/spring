package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.Team;
import com.spring.rest.meetingscheduler.request.TeamRequestObject;
import com.spring.rest.meetingscheduler.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class TeamController implements TeamOperations{

    private TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @Override
    public List<Team> getAllTeams() {
        return teamService.findAll();
    }

    @Override
    public Team createTeam(Team team) {
        team.setTeamId(0);
        return teamService.save(team);
    }

    @Override
    public Team findById(int teamId) {
        Team team = teamService.findById(teamId);
        if (team == null)
            throw new RuntimeException("No such team");
        return team;
    }

    @Override
    public void deleteTeam(int teamId) {
        Team team = teamService.findById(teamId);
        if(team == null)
            throw new RuntimeException("No such Team");
        teamService.deleteById(teamId);
    }

    @Override
    public Team addEmployeeToTeam(TeamRequestObject object) {
        int teamID = object.getTeamId();
        List<Integer> employeeIds = object.getEmployees();
        System.out.println(employeeIds);
        return teamService.addEmployee(employeeIds, teamID);
    }
}
