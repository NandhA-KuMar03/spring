package com.spring.rest.meetingscheduler.repository;

import com.spring.rest.meetingscheduler.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {
    Optional<Meeting> findByMeetingId(int meetingId);
}
