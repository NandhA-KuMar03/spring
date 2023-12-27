package com.spring.rest.meetingscheduler.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@JsonInclude( value = JsonInclude.Include.NON_DEFAULT,content = JsonInclude.Include.NON_NULL)
public class MeetingResponse {

    private int meetingId;
    private String meetingName;
    private Date meetingDate;
    private Time meetingStartTime;
    private Time meetingEndTime;
    private List<Long> employeeId;
    private String roomName;
    private int roomId;

    public MeetingResponse() {
    }

    public MeetingResponse(String meetingName, Date meetingDate, Time meetingStartTime, Time meetingEndTime, List<Long> employeeId, String roomName, int roomId, int meetingId) {
        this.meetingName = meetingName;
        this.meetingDate = meetingDate;
        this.meetingStartTime = meetingStartTime;
        this.meetingEndTime = meetingEndTime;
        this.employeeId = employeeId;
        this.roomName = roomName;
        this.roomId = roomId;
        this.meetingId = meetingId;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public Time getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(Time meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Time getMeetingEndTime() {
        return meetingEndTime;
    }

    public void setMeetingEndTime(Time meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }

    public List<Long> getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(List<Long> employeeId) {
        this.employeeId = employeeId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    @Override
    public String toString() {
        return "MeetingResponse{" +
                "meetingName='" + meetingName + '\'' +
                ", meetingDate=" + meetingDate +
                ", meetingStartTime=" + meetingStartTime +
                ", meetingEndTime=" + meetingEndTime +
                ", employeeId=" + employeeId +
                ", roomName-" + roomName +
                ", roomId-" + roomId +
                ", meetingId-" + meetingId +
                '}';
    }
}
