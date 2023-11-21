package com.spring.rest.meetingscheduler.repository;

import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Integer> {
    MeetingRoom findByMeetingRoomId(int roomId);
}
