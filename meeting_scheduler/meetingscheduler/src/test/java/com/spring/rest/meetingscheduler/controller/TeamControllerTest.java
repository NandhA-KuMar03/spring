package com.spring.rest.meetingscheduler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.rest.meetingscheduler.entity.Team;
import com.spring.rest.meetingscheduler.request.TeamRequestObject;
import com.spring.rest.meetingscheduler.service.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(TeamController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TeamControllerTest {

    @MockBean
    TeamService teamService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllTeams() throws Exception {
        List<Team> teams = new ArrayList<>();
        Team team = new Team("spring");
        team.setTeamId(1);
        teams.add(team);
        when(teamService.findAll()).thenReturn(teams);
        mockMvc.perform(get("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void createTeam() throws Exception{
        Team team = new Team("spring");
        team.setTeamId(1);
        when(teamService.save(team)).thenReturn(team);
        mockMvc.perform(post("/api/teams")
                        .content(new ObjectMapper().writeValueAsString(team))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void findByIdTeam() throws Exception{
        Team team = new Team("spring");
        team.setTeamId(1);
        when(teamService.findById(1)).thenReturn(team);
        mockMvc.perform(get("/api/teams")
                        .param("roomId", String.valueOf(1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteById() throws Exception{
        Team team = new Team("spring");
        team.setTeamId(1);
        when(teamService.findById(1)).thenReturn(team);
        mockMvc.perform(delete("/api/teams/{teamId}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    @Test
    public void addEmployee() throws Exception{
        Team team = new Team("spring");
        team.setTeamId(1);
        List<Integer> emp = Arrays.asList(1,2,3);
        TeamRequestObject object = new TeamRequestObject(1, emp);
        when(teamService.addEmployee(emp, 1)).thenReturn(team);
        mockMvc.perform(put("/api/teams/employee")
                        .content(new ObjectMapper().writeValueAsString(object))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
