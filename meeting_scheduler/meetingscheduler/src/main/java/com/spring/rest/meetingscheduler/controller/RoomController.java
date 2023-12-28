package com.spring.rest.meetingscheduler.controller;

import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void deleteRoom(int roomId) {
        MeetingRoom meetingRoom = meetingRoomService.findById(roomId);
        if(meetingRoom == null)
            throw new RuntimeException("No such Meeting Room");
        meetingRoomService.deleteById(roomId);
    }
}
