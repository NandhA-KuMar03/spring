package com.spring.rest.meetingscheduler.serviceimpl;

import com.spring.rest.meetingscheduler.dao.RoomDAO;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.repository.MeetingRoomRepository;
import com.spring.rest.meetingscheduler.service.MeetingRoomService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {


    private RoomDAO roomDAO;
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    public MeetingRoomServiceImpl(RoomDAO roomDAO, MeetingRoomRepository meetingRoomRepository) {
        this.roomDAO = roomDAO;
        this.meetingRoomRepository = meetingRoomRepository;
    }

    @Override
    public List<MeetingRoom> findAll() {
        return roomDAO.findAll();
    }

    @Override
    public MeetingRoom findById(int id) {
        return roomDAO.findById(id);
    }

    @Transactional
    @Override
    public MeetingRoom save(MeetingRoom meetingRoom) {
        return roomDAO.save(meetingRoom);
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        roomDAO.deleteById(id);
    }
}
