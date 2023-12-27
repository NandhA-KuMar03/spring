package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.response.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface RoomOperations {

    @GetMapping("/rooms")
    List<MeetingRoom> getAllRooms();

    @PostMapping("/rooms")
    MeetingRoom createRoom(@RequestBody MeetingRoom meetingRoom);

    @GetMapping("/rooms/{roomId}")
    MeetingRoom findById(@PathVariable int roomId);

    @DeleteMapping("/rooms/{roomId}")
    ResponseEntity<CommonResponse> deleteTeam(@PathVariable int roomId);

}
