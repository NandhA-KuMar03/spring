package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import com.spring.rest.meetingscheduler.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MeetingController {
    private MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping("/availability")
    public HashMap<String,Integer> getAvailability(@RequestBody MeetingDetail meetingDetail, @RequestParam Optional<Integer> count){
        Date date = meetingDetail.getMeetingDate();
        Time startTime = meetingDetail.getMeetingStartTime();
        Time endTime = meetingDetail.getMeetingEndTime();
        HashMap<String, Integer> map = meetingService.getAvailableRooms(date, startTime, endTime, count.orElse(0));
        return map;
    }

    @PostMapping("/meeting")
    public String createMeeting(@RequestBody MeetingDetail meetingDetail, @RequestParam int teamId, @RequestParam int roomId, @RequestParam String meetingName ){
        Date date = meetingDetail.getMeetingDate();
        Time startTime = meetingDetail.getMeetingStartTime();
        Time endTime = meetingDetail.getMeetingEndTime();
        return meetingService.createMeeting(date, startTime, endTime, roomId, teamId, meetingName);
    }

    @DeleteMapping("cancel")
    public String cancelMeeting(@RequestParam int meetingId){
        return meetingService.cancelMeeting(meetingId);
    }
}
