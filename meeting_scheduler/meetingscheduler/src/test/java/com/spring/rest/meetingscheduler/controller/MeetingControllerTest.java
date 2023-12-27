package com.spring.rest.meetingscheduler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.rest.meetingscheduler.entity.MeetingRequestObject;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.response.MeetingResponse;
import com.spring.rest.meetingscheduler.response.MeetingsOnSpecificDateResponse;
import com.spring.rest.meetingscheduler.service.MeetingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MeetingControllerTest {

    @MockBean
    private MeetingService meetingService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAvailability() throws Exception {
        List<MeetingRoom> rooms = new ArrayList<>();
        MeetingRoom meetingRoom1 = new MeetingRoom("Tanjore", 4);
        MeetingRoom meetingRoom2 = new MeetingRoom("London", 8);
        rooms.add(meetingRoom2);
        rooms.add(meetingRoom1);
        MeetingRequestObject meetingDetailRequest = new MeetingRequestObject();
        meetingDetailRequest.setMeetingDate("2023-11-22");
        meetingDetailRequest.setMeetingStartTime("18:00:00");
        meetingDetailRequest.setMeetingEndTime("19:00:00");
        when(meetingService.getAvailableRooms(meetingDetailRequest)).thenReturn(rooms);
        mockMvc.perform(get("/api/availability")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(meetingDetailRequest)))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$[0].roomName").value("Tanjore"));
//        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void createMeeting() throws Exception{
        MeetingResponse response = new MeetingResponse();
        response.setMeetingId(1);
        MeetingRequestObject meetingDetailRequest = new MeetingRequestObject();
        meetingDetailRequest.setMeetingDate("2023-11-22");
        meetingDetailRequest.setMeetingStartTime("18:00:00");
        meetingDetailRequest.setMeetingEndTime("19:00:00");
        meetingDetailRequest.setTeamId(1);
        meetingDetailRequest.setRoomId(1);
        meetingDetailRequest.setMeetingName("Mentors meeting");
        when(meetingService.createMeeting(meetingDetailRequest)).thenReturn(response);
        mockMvc.perform(post("/api/meeting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(meetingDetailRequest)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void cancelMeeting() throws Exception{
        MeetingResponse response = new MeetingResponse();
        response.setMeetingId(1);
        when(meetingService.cancelMeeting(1)).thenReturn(response);
        mockMvc.perform(delete("/api/meeting")
                        .param("meetingId", String.valueOf(1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateMeeting() throws Exception{
        MeetingResponse response = new MeetingResponse();
        response.setMeetingId(1);
        when(meetingService.updateMeeting(1,null,"Scrum", null, Time.valueOf("18:30:00"))).thenReturn(response);
        mockMvc.perform(patch("/api/meeting/dateTime")
                        .param("meetingId", String.valueOf(1))
                .param("meetingName", "Scrum")
                        .param("endTime", "18:30:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateRoom() throws Exception{
        MeetingResponse response = new MeetingResponse();
        response.setMeetingId(1);
        when(meetingService.updateRoom(1,2)).thenReturn(response);
        mockMvc.perform(patch("/api/meeting/room")
                .param("meetingId", String.valueOf(1))
                .param("roomId", String.valueOf(1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void changePeople() throws Exception {
        MeetingResponse response = new MeetingResponse();
        response.setMeetingId(1);
        List<Integer> addPeople = Arrays.asList(2, 3);
        when(meetingService.updatePeople(2, addPeople, null)).thenReturn(response);
        mockMvc.perform(patch("/api/meeting/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("meetingId", String.valueOf(1))
                        .param("addPeople", new String[]{"2", "3"}))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getMeetings() throws Exception{
        List<MeetingsOnSpecificDateResponse> responses = new ArrayList<>();
        MeetingResponse meeting1 = new MeetingResponse();
        meeting1.setMeetingId(1);
        MeetingResponse meeting2 = new MeetingResponse();
        meeting2.setMeetingId(2);
        MeetingResponse meeting3 = new MeetingResponse();
        meeting3.setMeetingId(3);
        when(meetingService.getMeetings(Date.valueOf("2023-11-29"))).thenReturn(responses);
        mockMvc.perform(get("/api/meeting/date")
                .contentType(MediaType.APPLICATION_JSON)
                .param("date", String.valueOf(Date.valueOf("2023-11-29"))))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
