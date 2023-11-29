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
    List<MeetingDetail> findByMeetingMeetingId(long meetingId);
    MeetingDetail findByEmployeeEmployeeIdAndMeetingMeetingId(long id, long meetingId);
}

