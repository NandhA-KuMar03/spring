package com.application.gatekeeper.controller;

import com.application.gatekeeper.entity.Blacklist;
import com.application.gatekeeper.entity.Role;
import com.application.gatekeeper.entity.User;
import com.application.gatekeeper.entity.UserDetails;
import com.application.gatekeeper.entity.Visitor;
import com.application.gatekeeper.entity.VisitorDetails;
import com.application.gatekeeper.request.BlacklistRequest;
import com.application.gatekeeper.request.LoginRequest;
import com.application.gatekeeper.request.RegisterRequest;
import com.application.gatekeeper.request.VisitorRegisterRequest;
import com.application.gatekeeper.request.VisitorRequest;
import com.application.gatekeeper.response.AuthenticationResponse;
import com.application.gatekeeper.service.AdminService;
import com.application.gatekeeper.service.ResidentService;
import com.application.gatekeeper.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResidentControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ResidentController residentController;

    @MockBean
    private ResidentService residentService;
    @MockBean
    private UserService userService;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(residentController).build();
    }

    @Test
    public void register() throws Exception{
        RegisterRequest request = new RegisterRequest("email@e.com", "pass", "fName", "lName", "M", Date.valueOf("2001-01-01"), "a101");
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email1@e.com", "pass", true, true, roles);
        UserDetails userDetails1 = new UserDetails(1, user1, "first", "lastName", "m", Date.valueOf("2002-01-01"), "a101");
        when(userService.createUser(request)).thenReturn(userDetails1);
        MvcResult mvcResult = mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userFirstName").value("first"))
                .andReturn();
    }

    @Test
    public void login() throws Exception{
        LoginRequest request = new LoginRequest();
        request.setEmail("email");
        request.setPassword("pass");
        AuthenticationResponse response = new AuthenticationResponse("wudshcbweud");
        when(userService.login(request)).thenReturn(response);
        MvcResult mvcResult = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.authToken").value("wudshcbweud"))
                .andReturn();
    }

    @Test
    public void createAndScheduleVisitor() throws Exception{
        VisitorRegisterRequest request = new VisitorRegisterRequest("v1@v.com", "Visitor");
        Visitor visitor = new Visitor(1, request.getName(), request.getEmail(), false);
        when(residentService.createAndScheduleVisitor(request)).thenReturn(visitor);
        MvcResult mvcResult = mockMvc.perform(post("/api/resident/visitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("v1@v.com"))
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void scheduleVisitor() throws Exception{
        VisitorRequest request = new VisitorRequest("v1@v.com", Date.valueOf("2024-02-29"), Time.valueOf("19:00:00"), Time.valueOf("19:30:00"));
        Visitor visitor = new Visitor(1, "name", request.getEmail(), false);
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        VisitorDetails visitorDetails = new VisitorDetails(0, visitor, request.getDateOfVisit(), "passkey", false, user1, request.getTimeOfEntry(), request.getTimeOfExit(), false);
        when(residentService.scheduleVisitor(request)).thenReturn(visitorDetails);
        MvcResult mvcResult = mockMvc.perform(post("/api/resident/visitor/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.approved").value("false"))
                .andReturn();
    }

    @Test
    public void blackListVisitor() throws Exception{
        BlacklistRequest request = new BlacklistRequest("v1@v.com", "Crime");
        Visitor visitor = new Visitor(1, "name", request.getEmail(), false);
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        Blacklist blacklist = new Blacklist(0, visitor, user1, "Crime");
        when(residentService.blackListVisitor(request)).thenReturn(blacklist);
        MvcResult mvcResult = mockMvc.perform(put("/api/resident/blacklist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("Crime"))
                .andReturn();
    }

    @Test
    public void getVisitors() throws Exception{
        Visitor visitor = new Visitor(1, "name", "name", false);
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor, Date.valueOf("2024-02-28"), "passkey", false, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        VisitorDetails visitorDetails2 = new VisitorDetails(2, visitor, Date.valueOf("2024-02-28"), "passkey", false, user1, Time.valueOf("19:25:00"), Time.valueOf("19:50:00"), false);
        when(residentService.getVisitors()).thenReturn(List.of(visitorDetails1, visitorDetails2));
        MvcResult mvcResult = mockMvc.perform(get("/api/resident/visitors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitorDetailId").value("1"))
                .andExpect(jsonPath("$.*", Matchers.hasSize(2)))
                .andReturn();
    }

    @Test
    public void getActiveVisitors() throws Exception{
        Visitor visitor = new Visitor(1, "name", "name", false);
        Role role2 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role2);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        VisitorDetails visitorDetails1 = new VisitorDetails(1, visitor, Date.valueOf("2024-02-28"), "passkey", false, user1, Time.valueOf("19:00:00"), Time.valueOf("19:20:00"), false);
        VisitorDetails visitorDetails2 = new VisitorDetails(2, visitor, Date.valueOf("2024-02-28"), "passkey", false, user1, Time.valueOf("19:25:00"), Time.valueOf("19:50:00"), false);
        when(residentService.getActiveVisitors()).thenReturn(List.of(visitorDetails1, visitorDetails2));
        MvcResult mvcResult = mockMvc.perform(get("/api/resident/visitors/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitorDetailId").value("1"))
                .andExpect(jsonPath("$.*", Matchers.hasSize(2)))
                .andReturn();
    }





}
