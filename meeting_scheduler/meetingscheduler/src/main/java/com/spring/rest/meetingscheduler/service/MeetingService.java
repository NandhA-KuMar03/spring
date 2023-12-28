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

    MeetingResponse updateMeeting(MeetingRequestObject object);

    MeetingResponse updateRoom(MeetingRequestObject object);

    MeetingResponse updatePeople(MeetingRequestObject object);

    List<MeetingsOnSpecificDateResponse> getMeetings(Date date);
}
