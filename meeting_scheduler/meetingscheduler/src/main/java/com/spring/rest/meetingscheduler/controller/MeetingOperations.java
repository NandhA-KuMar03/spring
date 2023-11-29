package com.spring.rest.meetingscheduler.controller;


import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface MeetingOperations {

    @GetMapping("/availability")
    HashMap<String,Integer> getAvailability(@RequestBody MeetingDetail meetingDetail, @RequestParam Optional<Integer> count);

    @PostMapping("/meeting")
    String createMeeting(@RequestBody MeetingDetail meetingDetail, @RequestParam int teamId, @RequestParam int roomId, @RequestParam String meetingName );

    @DeleteMapping("/cancel")
    String cancelMeeting(@RequestParam int meetingId);

    @PatchMapping("/update")
    String updateMeeting(@RequestParam int meetingId, @RequestParam Optional<Date> date, @RequestParam Optional<String> meetingName, @RequestParam Optional<Time> startTime, @RequestParam Optional<Time> endTime);

    @PatchMapping("/changeRoom")
    String updateRoom(@RequestParam int meetingId, @RequestParam int roomId);

    @PatchMapping("/updatePeople")
    String changePeople(@RequestParam int meetingId, @RequestParam Optional<List<Integer>> addPeople, @RequestParam Optional<List<Integer>> removePeople);

}
