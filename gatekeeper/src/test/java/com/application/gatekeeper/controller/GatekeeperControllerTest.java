package com.application.gatekeeper.controller;

import com.application.gatekeeper.entity.Blacklist;
import com.application.gatekeeper.entity.Role;
import com.application.gatekeeper.entity.User;
import com.application.gatekeeper.entity.Visitor;
import com.application.gatekeeper.entity.VisitorDetails;
import com.application.gatekeeper.request.ApproveVisitorRequest;
import com.application.gatekeeper.request.BlacklistRequest;
import com.application.gatekeeper.service.GatekeeperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GatekeeperControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private GatekeeperController gatekeeperController;

    @MockBean
    private GatekeeperService gatekeeperService;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(gatekeeperController).build();
    }

    @Test
    public void getVisitorsOnDate() throws Exception{
        Role role1 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        Visitor visitor1 = new Visitor(1,"visName", "v1@v.com", false);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor1, Date.valueOf("2024-01-29"), "passkey", false, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        VisitorDetails visitorDetails2 = new VisitorDetails(2, visitor1, Date.valueOf("2024-01-29"), "passkey", false, user1, Time.valueOf("19:35:00"), Time.valueOf("19:50:00"), false);
        when(gatekeeperService.getVisitorsOnDate(Date.valueOf("2024-01-29"))).thenReturn(List.of(visitorDetails1, visitorDetails2));
        MvcResult mvcResult = mockMvc.perform(get("/api/gatekeeper/visitors")
                .param("date", String.valueOf("2024-01-29")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitorDetailId").value("1"))
                .andReturn();
    }

    @Test
    public void respondToVisitorScheduleRequest() throws Exception{
        ApproveVisitorRequest request = new ApproveVisitorRequest(1,"true");
        Role role1 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        Visitor visitor1 = new Visitor(1,"visName", "v1@v.com", false);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor1, Date.valueOf("2024-01-29"), "passkey", true, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        VisitorDetails visitorDetails2 = new VisitorDetails(2, visitor1, Date.valueOf("2024-01-29"), "passkey", false, user1, Time.valueOf("19:35:00"), Time.valueOf("19:50:00"), false);
        when(gatekeeperService.respondToVisitorScheduleRequest(request)).thenReturn(visitorDetails1);
        MvcResult mvcResult = mockMvc.perform(patch("/api/gatekeeper/visitors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitorDetailId").value("1"))
                .andExpect(jsonPath("$.approved").value("true"))
                .andReturn();
    }

    @Test
    public void getAllVisitorDetailsOfParticularVisitor() throws Exception{
        Role role1 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        Visitor visitor1 = new Visitor(1,"visName", "v1@v.com", false);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor1, Date.valueOf("2024-01-29"), "passkey", true, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        VisitorDetails visitorDetails2 = new VisitorDetails(2, visitor1, Date.valueOf("2024-01-29"), "passkey", false, user1, Time.valueOf("19:35:00"), Time.valueOf("19:50:00"), false);
        when(gatekeeperService.getAllVisitorDetailsOfParticularVisitor(1)).thenReturn(List.of(visitorDetails1, visitorDetails2));
        MvcResult mvcResult = mockMvc.perform(get("/api/gatekeeper/visitor/history")
                        .param("visitorId", String.valueOf("1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitorDetailId").value("1"))
                .andExpect(jsonPath("$[0].approved").value("true"))
                .andExpect(jsonPath("$.*", Matchers.hasSize(2)))
                .andReturn();
    }

    @Test
    public void getAllVisitorDetailsOfParticularVisitorActiveData() throws Exception{
        Role role1 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        Visitor visitor1 = new Visitor(1,"visName", "v1@v.com", false);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor1, Date.valueOf("2024-01-29"), "passkey", true, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        VisitorDetails visitorDetails2 = new VisitorDetails(2, visitor1, Date.valueOf("2024-01-29"), "passkey", false, user1, Time.valueOf("19:35:00"), Time.valueOf("19:50:00"), false);
        VisitorDetails visitorDetails3 = new VisitorDetails(3, visitor1, Date.valueOf("2024-01-22"), "passkey", true, user1, Time.valueOf("19:35:00"), Time.valueOf("19:50:00"), true);
        when(gatekeeperService.getAllVisitorDetailsOfParticularVisitorActiveData(1)).thenReturn(List.of(visitorDetails1, visitorDetails2));
        MvcResult mvcResult = mockMvc.perform(get("/api/gatekeeper/visitor/active")
                        .param("visitorId", String.valueOf("1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitorDetailId").value("1"))
                .andExpect(jsonPath("$[0].approved").value("true"))
                .andExpect(jsonPath("$.*", Matchers.hasSize(2)))
                .andReturn();
    }

    @Test
    public void blackListVisitor() throws Exception{
        BlacklistRequest request = new BlacklistRequest("bl@email.com", "Crime");
        Role role1 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        Visitor visitor1 = new Visitor(1,"visName", "bl@email.com", false);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor1, Date.valueOf("2024-01-29"), "passkey", true, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        VisitorDetails visitorDetails2 = new VisitorDetails(2, visitor1, Date.valueOf("2024-01-29"), "passkey", false, user1, Time.valueOf("19:35:00"), Time.valueOf("19:50:00"), false);
        Blacklist blacklist = new Blacklist(0, visitor1, user1, request.getReason());
        when(gatekeeperService.blackListVisitor(request)).thenReturn(blacklist);
        MvcResult mvcResult = mockMvc.perform(put("/api/gatekeeper/blacklist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("Crime"))
                .andReturn();
    }
}
