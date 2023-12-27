package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.Meeting;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.response.CommonResponse;
import com.spring.rest.meetingscheduler.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoomController implements RoomOperations{

    private MeetingRoomService meetingRoomService;

    @Autowired
    public RoomController(MeetingRoomService meetingRoomService) {
        this.meetingRoomService = meetingRoomService;
    }

    @Override
    public List<MeetingRoom> getAllRooms() {
        return meetingRoomService.findAll();
    }

    @Override
    public MeetingRoom createRoom(MeetingRoom meetingRoom) {
        meetingRoom.setMeetingRoomId(0);
        MeetingRoom meetingRoom1 = meetingRoomService.save(meetingRoom);
        return meetingRoom1;
    }

    @Override
    public MeetingRoom findById(int roomId) {
        MeetingRoom meetingRoom = meetingRoomService.findById(roomId);
        if(meetingRoom == null)
            throw new RuntimeException("No such Meeting Room");
        return meetingRoom;
    }

    @Override
    public ResponseEntity<CommonResponse> deleteTeam(int roomId) {
        MeetingRoom meetingRoom = meetingRoomService.findById(roomId);
        if(meetingRoom == null)
            throw new RuntimeException("No such Meeting Room");
        meetingRoomService.deleteById(roomId);
        CommonResponse response = new CommonResponse();
        response.setStatusCode(HttpStatus.OK.value());
        response.setStatusMessage("Meeting Room Deleted");
        ResponseEntity<CommonResponse> commonResponseResponseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        return commonResponseResponseEntity;
    }
}
