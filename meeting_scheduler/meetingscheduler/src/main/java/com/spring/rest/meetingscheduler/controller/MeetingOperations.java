package com.spring.rest.meetingscheduler.controller;


import com.spring.rest.meetingscheduler.entity.MeetingRequestObject;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.response.MeetingResponse;
import com.spring.rest.meetingscheduler.response.MeetingsOnSpecificDateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface MeetingOperations {

    @GetMapping("/availability")
    ResponseEntity<List<MeetingRoom>> getAvailability(@RequestBody MeetingRequestObject meetingDetail);

    @PostMapping("/meeting")
    ResponseEntity<MeetingResponse> createMeeting(@RequestBody MeetingRequestObject meetingDetail);

    @DeleteMapping("/meeting")
    ResponseEntity<MeetingResponse> cancelMeeting(@RequestParam int meetingId);

    @PatchMapping("/meeting/dateTime")
    ResponseEntity<MeetingResponse> updateMeeting(@RequestParam int meetingId, @RequestParam(required = false) Date date, @RequestParam(required = false)String meetingName, @RequestParam(required = false) Time startTime, @RequestParam(required = false) Time endTime);

    @PatchMapping("/meeting/room")
    ResponseEntity<MeetingResponse> updateRoom(@RequestParam int meetingId, @RequestParam int roomId);

    @PatchMapping("/meeting/people")
    ResponseEntity<MeetingResponse> changePeople(@RequestParam int meetingId, @RequestParam(defaultValue = "")List<Integer> addPeople, @RequestParam(defaultValue = "")List<Integer> removePeople);

    @GetMapping("/meeting/date")
    ResponseEntity<List<MeetingsOnSpecificDateResponse>> getMeetings(@RequestParam Date date);
}
