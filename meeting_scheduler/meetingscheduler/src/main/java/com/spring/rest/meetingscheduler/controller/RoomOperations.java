package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface RoomOperations {

    @GetMapping("/rooms")
    List<MeetingRoom> getAllRooms();

    @PostMapping("/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    MeetingRoom createRoom(@RequestBody MeetingRoom meetingRoom);

    @GetMapping("/rooms/{roomId}")
    MeetingRoom findById(@PathVariable int roomId);

    @DeleteMapping("/rooms/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteRoom(@PathVariable int roomId);

}
