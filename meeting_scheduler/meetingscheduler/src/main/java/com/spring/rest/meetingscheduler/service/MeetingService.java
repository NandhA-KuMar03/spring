package com.spring.rest.meetingscheduler.service;

import java.sql.Time;
import java.sql.Date;
import java.util.HashMap;

public interface MeetingService {

    public HashMap<String, Integer> getAvailableRooms(Date date, Time startTime, Time endTime, int count);

    public String createMeeting(Date date, Time startTime, Time endTime, int roomId, int teamId, String meetingName);

    public String cancelMeeting(int meetingId);
}
