package com.spring.rest.meetingscheduler.controller;


import com.spring.rest.meetingscheduler.entity.MeetingRequestObject;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.response.MeetingResponse;
import com.spring.rest.meetingscheduler.response.MeetingsOnSpecificDateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface MeetingOperations {

    @GetMapping("/availability")
    ResponseEntity<List<MeetingRoom>> getAvailability(@RequestBody MeetingRequestObject meetingDetail);

    @PostMapping("/meeting")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<MeetingResponse> createMeeting(@RequestBody MeetingRequestObject meetingDetail);

    @DeleteMapping("/meeting")
    ResponseEntity<MeetingResponse> cancelMeeting(@RequestParam int meetingId);

    @PutMapping("/meeting/dateTime")
    ResponseEntity<MeetingResponse> updateMeeting(@RequestBody MeetingRequestObject object);

    @PatchMapping("/meeting/room")
    ResponseEntity<MeetingResponse> updateRoom(@RequestBody MeetingRequestObject object);

    @PutMapping("/meeting/people")
    ResponseEntity<MeetingResponse> changePeople(@RequestBody MeetingRequestObject object);

    @GetMapping("/meeting/date")
    ResponseEntity<List<MeetingsOnSpecificDateResponse>> getMeetings(@RequestParam Date date);
}
