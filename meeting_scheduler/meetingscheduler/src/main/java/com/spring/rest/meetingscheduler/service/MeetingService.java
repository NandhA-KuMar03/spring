package com.spring.rest.meetingscheduler.service;

import com.spring.rest.meetingscheduler.entity.MeetingDetail;
import com.spring.rest.meetingscheduler.entity.MeetingRequestObject;
import com.spring.rest.meetingscheduler.entity.MeetingRoom;
import com.spring.rest.meetingscheduler.response.MeetingResponse;
import com.spring.rest.meetingscheduler.response.MeetingsOnSpecificDateResponse;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.sql.Date;
import java.util.List;

public interface MeetingService {

    public List<MeetingRoom> getAvailableRooms(MeetingRequestObject meetingDetail);

    public MeetingResponse createMeeting(MeetingRequestObject meetingDetail);

    public MeetingResponse cancelMeeting(int meetingId);

    MeetingResponse updateMeeting(int meetingId, Date date, String meetingName, Time startTime, Time endTime);

    MeetingResponse updateRoom(int meetingId, int roomId);

    MeetingResponse updatePeople(int meetingId, List<Integer> addPeople, List<Integer> removePeople);

    List<MeetingsOnSpecificDateResponse> getMeetings(Date date);
}
