package com.spring.rest.meetingscheduler.dao;

import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDAOImpl implements RoomDAO{

    private EntityManager entityManager;

    @Autowired
    public RoomDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<MeetingRoom> findAll() {
        TypedQuery<MeetingRoom> rooms = entityManager.createQuery("FROM MeetingRoom", MeetingRoom.class);
        return rooms.getResultList();
    }

    @Override
    public MeetingRoom findById(int id) {
        MeetingRoom meetingRoom = entityManager.find(MeetingRoom.class, id);
        return meetingRoom;
    }

    @Override
    public MeetingRoom save(MeetingRoom meetingRoom) {
        MeetingRoom meetingRoom1 = entityManager.merge(meetingRoom);
        return meetingRoom1;
    }

    @Override
    public void deleteById(int id) {
        MeetingRoom meetingRoom1 = entityManager.find(MeetingRoom.class, id);
        entityManager.remove(meetingRoom1);
    }
}
