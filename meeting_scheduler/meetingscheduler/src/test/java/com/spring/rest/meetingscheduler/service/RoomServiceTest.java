package com.spring.rest.meetingscheduler.service;

import com.spring.rest.meetingscheduler.dao.RoomDAO;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.serviceimpl.MeetingRoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {


    @InjectMocks
    MeetingRoomServiceImpl meetingRoomService;
    @Mock
    RoomDAO roomDAO;


    @Test
    void getMeetingRooms(){
        List<MeetingRoom> rooms = new ArrayList<>();
        MeetingRoom meetingRoom1 = new MeetingRoom("Tanjore", 4);
        meetingRoom1.setMeetingRoomId(1);
        rooms.add(meetingRoom1);
        when(roomDAO.findAll()).thenReturn(rooms);
        List<MeetingRoom> response = meetingRoomService.findAll();
        assertEquals(response.get(0).getMeetingRoomId(), rooms.get(0).getMeetingRoomId());
    }

    @Test
    void findById(){
        MeetingRoom meetingRoom1 = new MeetingRoom("Tanjore", 4);
        meetingRoom1.setMeetingRoomId(1);
        when(roomDAO.findById(1)).thenReturn(meetingRoom1);
        MeetingRoom response = meetingRoomService.findById(1);
        assertEquals(response,meetingRoom1);
    }

    @Test
    void saveEmployee(){
        MeetingRoom meetingRoom1 = new MeetingRoom("Tanjore", 4);
        meetingRoom1.setMeetingRoomId(1);
        when(roomDAO.save(meetingRoom1)).thenReturn(meetingRoom1);
        MeetingRoom response = meetingRoomService.save(meetingRoom1);
        assertEquals(response,meetingRoom1);
    }

}
