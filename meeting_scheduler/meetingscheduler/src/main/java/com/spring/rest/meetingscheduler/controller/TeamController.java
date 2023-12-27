package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.Team;
import com.spring.rest.meetingscheduler.response.CommonResponse;
import com.spring.rest.meetingscheduler.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CommonResponse> deleteTeam(int teamId) {
        Team team = teamService.findById(teamId);
        if(team == null)
            throw new RuntimeException("No such Team");
        teamService.deleteById(teamId);
        CommonResponse response = new CommonResponse();
        response.setStatusMessage("Team Deleted");
        response.setStatusCode(HttpStatus.OK.value());
        ResponseEntity<CommonResponse> commonResponseResponseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        return commonResponseResponseEntity;
    }

    @Override
    public Team addEmployeeToTeam(int employeeId, int teamID) {
        return teamService.addEmployee(employeeId, teamID);
    }
}
