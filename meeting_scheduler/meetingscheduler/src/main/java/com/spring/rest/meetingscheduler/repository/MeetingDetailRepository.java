package com.spring.rest.meetingscheduler.repository;

import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Repository
public interface MeetingDetailRepository extends JpaRepository<MeetingDetail, Integer> {
    List<MeetingDetail> findByMeetingDate(Date date);
    List<MeetingDetail> findByMeetingMeetingId(int meetingId);
    void deleteByEmployeeEmployeeId(int id);
    MeetingDetail findByEmployeeEmployeeIdAndMeetingMeetingId(int id, int meetingId);
}

