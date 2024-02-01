package com.application.gatekeeper.controller;

import com.application.gatekeeper.entity.Role;
import com.application.gatekeeper.entity.User;
import com.application.gatekeeper.entity.UserDetails;
import com.application.gatekeeper.request.RegisterRequest;
import com.application.gatekeeper.response.VisitorResponse;
import com.application.gatekeeper.service.ResidentService;
import com.application.gatekeeper.service.UserService;
import com.application.gatekeeper.service.VisitorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.application.gatekeeper.constants.CommonConstants.VISITOR_VISITED;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VisitorControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private VisitorController visitorController;
    @MockBean
    private VisitorService visitorService;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(visitorController).build();
    }

    @Test
    public void visitorEntry() throws Exception{
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        VisitorResponse response = new VisitorResponse(VISITOR_VISITED);
        when(visitorService.visitorEntry(request)).thenReturn(response);
        MvcResult mvcResult = mockMvc.perform(put("/api/entry"))
                .andExpect(status().isOk())
                .andReturn();
    }

}
