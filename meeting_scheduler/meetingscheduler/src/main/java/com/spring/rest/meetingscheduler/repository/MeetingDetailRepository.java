package com.spring.rest.meetingscheduler.repository;

import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Repository
public interface MeetingDetailRepository extends JpaRepository<MeetingDetail, Integer> {

//    List<MeetingDetail> findByMeetingDateAndMeetingStartTimeGreaterThanEqualMeetingEndTimeLessThanEqual(Date date, Time startTime, Time endTime);
    List<MeetingDetail> findByMeetingDate(Date date);
    List<MeetingDetail> findByMeetingMeetingId(int meetingId);
//    List<MeetingDetail> findByMeetingId(int meetingId);
//    List<MeetingDetail> findByMeetingStartTimeBetweenOrMeetingEndTimeBetween(Time start_time, Time end_time, Time start, Time end);
}

