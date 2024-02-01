package com.application.gatekeeper.controller;

import com.application.gatekeeper.entity.Role;
import com.application.gatekeeper.entity.User;
import com.application.gatekeeper.entity.UserDetails;
import com.application.gatekeeper.request.EditInfoRequest;
import com.application.gatekeeper.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private AdminController adminController;

    @MockBean
    private AdminService adminService;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void getAwaitingApprovals() throws Exception {
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", false, false, roles);
        User user2 = new User(2, "email@cc.com", "pass", false, false, roles);
        User user3 = new User(3, "email@cd.com", "pass", false, false, roles);
        when(adminService.getAwaitingApprovals()).thenReturn(List.of(user1, user2, user3));
        MvcResult mvcResult = mockMvc.perform(get("/api/admin/approvals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("1")).
                andReturn();
    }

    @Test
    public void getUserAwaitingApproval() throws Exception{
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", false, false, roles);
        UserDetails userDetails1 = new UserDetails(1, user1, "firstName", "lastName", "m", Date.valueOf("2002-01-01"), null);
        when(adminService.getUserAwaitingApproval(1)).thenReturn(userDetails1);
        MvcResult mvcResult = mockMvc.perform(get("/api/admin/approvals/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userDetailId").value("1")).
                andReturn();
    }

    @Test
    public void approveUser() throws Exception{
        Role role1 = new Role(1, "ROLE_GATEKEEPER");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        when(adminService.approveUser(1)).thenReturn(user1);
        MvcResult mvcResult = mockMvc.perform(put("/api/admin/approvals")
                        .param("userId", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("1")).
                andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void editInfo() throws Exception{
        EditInfoRequest request = new EditInfoRequest(1, "firstName", "lastName", Date.valueOf("2001-01-01"), "A101");
        Role role1 = new Role(1, "ROLE_RESIDENT");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        User user1 = new User(1, "email@e.com", "pass", true, true, roles);
        UserDetails userDetails1 = new UserDetails(1, user1, "firstName", "lastName", "m", Date.valueOf("2001-01-01"), "A101");
        when(adminService.getUserAwaitingApproval(1)).thenReturn(userDetails1);
        when(adminService.editInfo(request)).thenReturn(userDetails1);
        MvcResult mvcResult = mockMvc.perform(put("/api/admin/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userDetailId").value("1")).
                andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void removeUser() throws Exception{
        MvcResult mvcResult = mockMvc.perform(patch("/api/admin/user")
                        .param("userId", String.valueOf(1)))
                .andExpect(status().isNoContent())
                .andReturn();
    }


}
