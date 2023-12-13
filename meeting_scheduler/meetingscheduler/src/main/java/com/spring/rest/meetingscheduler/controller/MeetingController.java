package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import com.spring.rest.meetingscheduler.entity.MeetingRequestObject;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<MeetingRoom>> getAvailability(MeetingRequestObject meetingDetail, int count) {
        return meetingService.getAvailableRooms(meetingDetail,count);
    }

    //    Create meeting
    public String createMeeting(@RequestBody MeetingRequestObject meetingDetail, @RequestParam int teamId, @RequestParam int roomId, @RequestParam String meetingName ){
        return meetingService.createMeeting(meetingDetail, roomId, teamId, meetingName);
    }

//    Cancel meeting
    public String cancelMeeting(@RequestParam int meetingId){
        return meetingService.cancelMeeting(meetingId);
    }

//    Update date meeting name, start time, end time
    public String updateMeeting(@RequestParam int meetingId, @RequestParam(required = false) Date date, @RequestParam(required = false) String meetingName, @RequestParam(required = false) Time startTime, @RequestParam(required = false) Time endTime){
        return meetingService.updateMeeting(meetingId, date, meetingName, startTime, endTime);
    }

//    Change room
    public String updateRoom(@RequestParam int meetingId, @RequestParam int roomId){
        return meetingService.updateRoom(meetingId,roomId);
    }

//    Add or remove people
//    public String changePeople(@RequestParam int meetingId, @RequestParam Optional<List<Integer>> addPeople, @RequestParam Optional<List<Integer>> removePeople){
//        return meetingService.updatePeople(meetingId, addPeople, removePeople);
//    }

    @Override
    public String changePeople(int meetingId, List<Integer> addPeople, List<Integer> removePeople) {
        return meetingService.updatePeople(meetingId, addPeople, removePeople);
    }
}
