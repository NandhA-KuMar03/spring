package com.spring.rest.meetingscheduler.response;

import java.sql.Time;
import java.util.List;

public class MeetingsOnSpecificDateResponse {

    private int meetingId;
    private String meetingName;
    private int roomId;
    private String roomName;
    private Time startTime;
    private Time endTime;

    public MeetingsOnSpecificDateResponse() {
    }

    public MeetingsOnSpecificDateResponse(int meetingId, String meetingName, int roomId, String roomName, Time startTime, Time endTime) {
        this.meetingId = meetingId;
        this.meetingName = meetingName;
        this.roomId = roomId;
        this.roomName = roomName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }


    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "MeetingsOnSpecificDateResponse{" +
                "meetingId=" + meetingId +
                ", meetingName='" + meetingName + '\'' +
                ", roomId=" + roomId +
                ", roomName='" + roomName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
