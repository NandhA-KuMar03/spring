package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.Meeting;
import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.repository.MeetingRepository;
import com.spring.rest.meetingscheduler.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
public class MeetingController {
    private MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

//    Check availability
    @GetMapping("/availability")
    public HashMap<String,Integer> getAvailability(@RequestBody MeetingDetail meetingDetail, @RequestParam Optional<Integer> count){
        Date date = meetingDetail.getMeetingDate();
        Time startTime = meetingDetail.getMeetingStartTime();
        Time endTime = meetingDetail.getMeetingEndTime();
        HashMap<String, Integer> map = meetingService.getAvailableRooms(date, startTime, endTime, count.orElse(0));
        return map;
    }

//    Create meeting
    @PostMapping("/meeting")
    public String createMeeting(@RequestBody MeetingDetail meetingDetail, @RequestParam int teamId, @RequestParam int roomId, @RequestParam String meetingName ){
        Date date = meetingDetail.getMeetingDate();
        Time startTime = meetingDetail.getMeetingStartTime();
        Time endTime = meetingDetail.getMeetingEndTime();
        return meetingService.createMeeting(date, startTime, endTime, roomId, teamId, meetingName);
    }

//    Cancel meeting
    @DeleteMapping("/cancel")
    public String cancelMeeting(@RequestParam int meetingId){
        return meetingService.cancelMeeting(meetingId);
    }

//    Update date meeting name, start time, end time
    @PatchMapping("/update")
    public String updateMeeting(@RequestParam int meetingId, @RequestParam Optional<Date> date, @RequestParam Optional<String> meetingName, @RequestParam Optional<Time> startTime, @RequestParam Optional<Time> endTime){
        return meetingService.updateMeeting(meetingId, date, meetingName, startTime, endTime);
    }

//    Change room
    @PatchMapping("/changeRoom")
    public String updateRoom(@RequestParam int meetingId, @RequestParam int roomId){
        return meetingService.updateRoom(meetingId,roomId);
    }

//    Add or remove people
    @PatchMapping("/updatePeople")
    public String changePeople(@RequestParam int meetingId, @RequestParam Optional<List<Integer>> addPeople, @RequestParam Optional<List<Integer>> removePeople){
        return meetingService.updatePeople(meetingId, addPeople, removePeople);
    }
}
