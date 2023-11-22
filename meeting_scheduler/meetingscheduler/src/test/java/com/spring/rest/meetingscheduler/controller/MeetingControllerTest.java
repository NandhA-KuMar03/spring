package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import com.spring.rest.meetingscheduler.service.MeetingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
        MeetingDetail meetingDetailRequest = new MeetingDetail();
        meetingDetailRequest.setMeetingDate(new Date(2023-11-22));
        meetingDetailRequest.setMeetingStartTime(Time.valueOf(LocalTime.parse("18:00:00")));
        meetingDetailRequest.setMeetingEndTime(Time.valueOf(LocalTime.parse("19:00:00")));
        availabilityResponse.put("Tanjore",4);
        availabilityResponse.put("London",8);

        //When
        when(meetingService.getAvailableRooms(meetingDetailRequest.getMeetingDate(),meetingDetailRequest.getMeetingStartTime(),meetingDetailRequest.getMeetingEndTime(),4)).thenReturn(availabilityResponse);
        HashMap<String,Integer> response = meetingController.getAvailability(meetingDetailRequest, Optional.of(4));

        //Then
        assertEquals(OK, HttpStatus.valueOf(200));
        assertEquals(response,availabilityResponse);
    }

    @Test
    void createMeeting(){
        MeetingDetail meetingDetailRequest = new MeetingDetail();
        meetingDetailRequest.setMeetingDate(new Date(2023-11-22));
        meetingDetailRequest.setMeetingStartTime(Time.valueOf(LocalTime.parse("18:00:00")));
        meetingDetailRequest.setMeetingEndTime(Time.valueOf(LocalTime.parse("19:00:00")));
        String expectedResponse = "Saved";

        when(meetingService.createMeeting(meetingDetailRequest.getMeetingDate(),meetingDetailRequest.getMeetingStartTime(),meetingDetailRequest.getMeetingEndTime(),3,1500,"Scrum")).thenReturn(expectedResponse);
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
        Optional<Date> date = Optional.of(new Date(2023-11-22));
        Optional<Time> startTime = Optional.of(Time.valueOf(LocalTime.parse("18:30:00")));
        Optional<Time> endTime = Optional.of(Time.valueOf(LocalTime.parse("19:00:00")));
        when(meetingService.updateMeeting(4,date,Optional.of("Mentors meeting") ,startTime,endTime)).thenReturn(expectedResponse);

        String response = meetingController.updateMeeting(4, date, Optional.of("Mentors meeting"), startTime, endTime);
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
        Optional<List<Integer>> addPeople = Optional.of(List.of(3,4,5,6));
        int meetingId = 3;

        when(meetingService.updatePeople(meetingId, addPeople, Optional.empty())).thenReturn(expectedResponse);
        String response = meetingController.changePeople(meetingId, addPeople, Optional.empty());
        assertEquals(response, expectedResponse);
        assertEquals(OK, HttpStatus.valueOf(200));
    }

}
