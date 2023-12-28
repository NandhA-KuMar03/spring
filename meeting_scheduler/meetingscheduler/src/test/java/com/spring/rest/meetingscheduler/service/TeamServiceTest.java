package com.spring.rest.meetingscheduler.service;

import com.spring.rest.meetingscheduler.dao.TeamDAO;
import com.spring.rest.meetingscheduler.entity.Team;
import com.spring.rest.meetingscheduler.serviceimpl.TeamServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @InjectMocks
    TeamServiceImpl teamService;

    @Mock
    TeamDAO teamDAO;

    @Test
    void addEmployeesToTeam(){
        List<Integer> emp = Arrays.asList(1,2,3);
        Team team = new Team("spring");
        team.setTeamId(1);
        when(teamDAO.findById(1)).thenReturn(team);
        Team response = teamService.addEmployee(emp, 1);
        assertEquals(response.getTeamName(), team.getTeamName());
    }

    @Test
    void getTeams(){
        List<Team> teams = new ArrayList<>();
        Team team = new Team("spring");
        team.setTeamId(1);
        teams.add(team);
        when(teamDAO.findAll()).thenReturn(teams);
        List<Team> response = teamService.findAll();
        assertEquals(response.get(0).getTeamId(), teams.get(0).getTeamId());
    }

    @Test
    void findById(){
        Team team = new Team("spring");
        team.setTeamId(1);
        when(teamDAO.findById(1)).thenReturn(team);
        Team response = teamService.findById(1);
        assertEquals(response,team);
    }

    @Test
    void saveTeam(){
        Team team = new Team("spring");
        team.setTeamId(1);
        when(teamDAO.save(team)).thenReturn(team);
        Team response = teamService.save(team);
        assertEquals(response,team);
    }

}
