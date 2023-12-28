package com.spring.rest.meetingscheduler.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.service.MeetingRoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(RoomController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RoomControllerTest {

    @MockBean
    MeetingRoomService meetingRoomService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllRooms() throws Exception {
        List<MeetingRoom> rooms = new ArrayList<>();
        MeetingRoom meetingRoom1 = new MeetingRoom("Tanjore", 4);
        MeetingRoom meetingRoom2 = new MeetingRoom("London", 8);
        rooms.add(meetingRoom2);
        rooms.add(meetingRoom1);
        when(meetingRoomService.findAll()).thenReturn(rooms);
        mockMvc.perform(get("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createRoom() throws Exception{
        MeetingRoom meetingRoom1 = new MeetingRoom("Tanjore", 4);
        MeetingRoom meetingRoom2 = new MeetingRoom("London", 8);
        meetingRoom1.setMeetingRoomId(1);
        when(meetingRoomService.save(meetingRoom1)).thenReturn(meetingRoom1);
        mockMvc.perform(post("/api/rooms")
                .content(new ObjectMapper().writeValueAsString(meetingRoom1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void findByIdRoom() throws Exception{
        MeetingRoom meetingRoom1 = new MeetingRoom("Tanjore", 4);
        meetingRoom1.setMeetingRoomId(1);
        when(meetingRoomService.findById(1)).thenReturn(meetingRoom1);
        mockMvc.perform(get("/api/rooms")
                        .param("roomId", String.valueOf(1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteById() throws Exception{
        MeetingRoom meetingRoom1 = new MeetingRoom("Tanjore", 4);
        meetingRoom1.setMeetingRoomId(1);
        when(meetingRoomService.findById(1)).thenReturn(meetingRoom1);
        mockMvc.perform(delete("/api/rooms/{roomId}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}
