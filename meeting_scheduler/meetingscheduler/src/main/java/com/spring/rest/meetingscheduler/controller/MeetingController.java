package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import com.spring.rest.meetingscheduler.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MeetingController implements  MeetingOperations{
    private MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

//    Check availability
    public HashMap<String,Integer> getAvailability(@RequestBody MeetingDetail meetingDetail, @RequestParam Optional<Integer> count){
        HashMap<String, Integer> map = meetingService.getAvailableRooms(meetingDetail, count.orElse(0));
        return map;
    }

//    Create meeting
    public String createMeeting(@RequestBody MeetingDetail meetingDetail, @RequestParam int teamId, @RequestParam int roomId, @RequestParam String meetingName ){
        return meetingService.createMeeting(meetingDetail, roomId, teamId, meetingName);
    }

//    Cancel meeting
    public String cancelMeeting(@RequestParam int meetingId){
        return meetingService.cancelMeeting(meetingId);
    }

//    Update date meeting name, start time, end time
    public String updateMeeting(@RequestParam int meetingId, @RequestParam Optional<Date> date, @RequestParam Optional<String> meetingName, @RequestParam Optional<Time> startTime, @RequestParam Optional<Time> endTime){
        return meetingService.updateMeeting(meetingId, date, meetingName, startTime, endTime);
    }

//    Change room
    public String updateRoom(@RequestParam int meetingId, @RequestParam int roomId){
        return meetingService.updateRoom(meetingId,roomId);
    }

//    Add or remove people
    public String changePeople(@RequestParam int meetingId, @RequestParam Optional<List<Integer>> addPeople, @RequestParam Optional<List<Integer>> removePeople){
        return meetingService.updatePeople(meetingId, addPeople, removePeople);
    }
}
