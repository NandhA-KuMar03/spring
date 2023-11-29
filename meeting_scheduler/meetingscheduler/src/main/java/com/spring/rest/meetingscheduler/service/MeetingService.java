package com.spring.rest.meetingscheduler.service;

import com.spring.rest.meetingscheduler.entity.MeetingDetail;

import java.sql.Time;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface MeetingService {

    public HashMap<String, Integer> getAvailableRooms(MeetingDetail meetingDetail, int count);

    public String createMeeting(MeetingDetail meetingDetail, int roomId, int teamId, String meetingName);

    public String cancelMeeting(int meetingId);

    String updateMeeting(int meetingId, Optional<Date> date, Optional<String> meetingName, Optional<Time> startTime, Optional<Time> endTime);

    String updateRoom(int meetingId, int roomId);

    String updatePeople(int meetingId, Optional<List<Integer>> addPeople, Optional<List<Integer>> removePeople);
}
