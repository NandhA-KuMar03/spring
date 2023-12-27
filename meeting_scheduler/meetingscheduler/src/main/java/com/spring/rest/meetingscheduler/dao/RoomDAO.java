package com.spring.rest.meetingscheduler.dao;

import com.spring.rest.meetingscheduler.entity.MeetingRoom;

import java.util.List;

public interface RoomDAO {

    List<MeetingRoom> findAll();

    MeetingRoom findById(int id);

    MeetingRoom save(MeetingRoom meetingRoom);

    void deleteById(int id);

}
