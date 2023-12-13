package com.spring.rest.meetingscheduler.service;

import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import com.spring.rest.meetingscheduler.entity.MeetingRequestObject;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.sql.Date;
import java.util.List;

public interface MeetingService {

    public ResponseEntity<List<MeetingRoom>> getAvailableRooms(MeetingRequestObject meetingDetail, int count);

    public String createMeeting(MeetingRequestObject meetingDetail, int roomId, int teamId, String meetingName);

    public String cancelMeeting(int meetingId);

    String updateMeeting(int meetingId, Date date, String meetingName, Time startTime, Time endTime);

    String updateRoom(int meetingId, int roomId);

    String updatePeople(int meetingId, List<Integer> addPeople, List<Integer> removePeople);
}
