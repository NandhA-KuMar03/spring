package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.MeetingRequestObject;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.response.MeetingResponse;
import com.spring.rest.meetingscheduler.response.MeetingsOnSpecificDateResponse;
import com.spring.rest.meetingscheduler.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MeetingController implements  MeetingOperations{
    private MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    //    Check availability
    @Override
    public ResponseEntity<List<MeetingRoom>> getAvailability(MeetingRequestObject meetingDetail) {
        return ResponseEntity.status(HttpStatus.OK).body(meetingService.getAvailableRooms(meetingDetail));
    }

    //    Create meeting
    public ResponseEntity<MeetingResponse> createMeeting(@RequestBody MeetingRequestObject meetingDetail){
        return ResponseEntity.status(HttpStatus.CREATED).body(meetingService.createMeeting(meetingDetail));
    }

//    Cancel meeting
    public ResponseEntity<MeetingResponse> cancelMeeting(@RequestParam int meetingId){
        return ResponseEntity.status(HttpStatus.OK).body(meetingService.cancelMeeting(meetingId));
    }

//    Update date meeting name, start time, end time
    public ResponseEntity<MeetingResponse> updateMeeting(@RequestParam int meetingId, @RequestParam(required = false) Date date, @RequestParam(required = false) String meetingName, @RequestParam(required = false) Time startTime, @RequestParam(required = false) Time endTime){
        return ResponseEntity.status(HttpStatus.OK).body(meetingService.updateMeeting(meetingId, date, meetingName, startTime, endTime));
    }

//    Change room
    public ResponseEntity<MeetingResponse> updateRoom(@RequestParam int meetingId, @RequestParam int roomId){
        return ResponseEntity.status(HttpStatus.OK).body(meetingService.updateRoom(meetingId,roomId));
    }

    @Override
    public ResponseEntity<MeetingResponse> changePeople(int meetingId, List<Integer> addPeople, List<Integer> removePeople) {
        return ResponseEntity.status(HttpStatus.OK).body(meetingService.updatePeople(meetingId, addPeople, removePeople));
    }

    @Override
    public ResponseEntity<List<MeetingsOnSpecificDateResponse>> getMeetings(@RequestParam Date date) {
        return ResponseEntity.status(HttpStatus.OK).body(meetingService.getMeetings(date));
    }
}
