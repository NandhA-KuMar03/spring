package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.MeetingRequestObject;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.service.MeetingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class MeetingControllerTest {
    @InjectMocks
    private MeetingController meetingController;
    @Mock
    private MeetingService meetingService;

    @Test
    void getAvailability(){
        //Given
        HashMap<String, Integer> availabilityResponse = new HashMap<>();
        MeetingRequestObject meetingDetailRequest = new MeetingRequestObject();
        meetingDetailRequest.setMeetingDate("2023-11-22");
        meetingDetailRequest.setMeetingStartTime("18:00:00");
        meetingDetailRequest.setMeetingEndTime("19:00:00");
        availabilityResponse.put("Tanjore",4);
        availabilityResponse.put("London",8);
        List<MeetingRoom> rooms = new ArrayList<>();
        MeetingRoom meetingRoom1 = new MeetingRoom("Tanjore", 4);
        MeetingRoom meetingRoom2 = new MeetingRoom("London", 8);
        rooms.add(meetingRoom2);
        rooms.add(meetingRoom1);
        ResponseEntity<List<MeetingRoom>> response1 = new ResponseEntity<>(rooms, HttpStatus.OK);

        //When
        when(meetingService.getAvailableRooms(meetingDetailRequest,4)).thenReturn(response1);
        ResponseEntity<List<MeetingRoom>> response = meetingController.getAvailability(meetingDetailRequest, 4);

        //Then
        assertEquals(OK, HttpStatus.valueOf(200));
        assertEquals(response,response1);
    }

    @Test
    void createMeeting(){
        MeetingRequestObject meetingDetailRequest = new MeetingRequestObject();
        meetingDetailRequest.setMeetingDate("2023-11-22");
        meetingDetailRequest.setMeetingStartTime("18:00:00");
        meetingDetailRequest.setMeetingEndTime("19:00:00");
        String expectedResponse = "Saved";

        when(meetingService.createMeeting(meetingDetailRequest,3,1500,"Scrum")).thenReturn(expectedResponse);
        String response = meetingController.createMeeting(meetingDetailRequest,1500, 3,"Scrum");

        assertEquals(OK, HttpStatus.valueOf(200));
        assertEquals(response,expectedResponse);
    }

    @Test
    void cancelMeeting(){
        String expectedResponse = "Canceled";
        int meetingId = 3;
        when(meetingService.cancelMeeting(meetingId)).thenReturn(expectedResponse);
        String response = meetingController.cancelMeeting(3);

        assertEquals(OK,HttpStatus.valueOf(200));
        assertEquals(response,expectedResponse);
    }

    @Test
    void updateDateTimeMeeting(){
        String expectedResponse = "Updated";
        Date date = new Date(2023-11-22);
        Time startTime = Time.valueOf(LocalTime.parse("18:30:00"));
        Time endTime = Time.valueOf(LocalTime.parse("19:00:00"));
        when(meetingService.updateMeeting(4,date,"Mentors meeting" ,startTime,endTime)).thenReturn(expectedResponse);

        String response = meetingController.updateMeeting(4, date, "Mentors meeting", startTime, endTime);
        assertEquals(response, expectedResponse);
        assertEquals(OK, HttpStatus.valueOf(200));
    }

    @Test
    void updateRoom(){
        String expectedResponse = "Room Changed";
        int meetingId = 3;
        int roomId = 1005;
        when(meetingService.updateRoom(meetingId,roomId)).thenReturn(expectedResponse);

        String response = meetingController.updateRoom(meetingId,roomId);
        assertEquals(response, expectedResponse);
        assertEquals(OK, HttpStatus.valueOf(200));
    }

    @Test
    void changePeople(){
        String expectedResponse = "People Updated";
        List<Integer> addPeople = List.of(3,4,5,6);
        int meetingId = 3;

        when(meetingService.updatePeople(meetingId, addPeople, Collections.emptyList())).thenReturn(expectedResponse);
        String response = meetingController.changePeople(meetingId, addPeople, Collections.emptyList());
        assertEquals(response, expectedResponse);
        assertEquals(OK, HttpStatus.valueOf(200));
    }

}
