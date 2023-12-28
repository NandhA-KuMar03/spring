package com.spring.rest.meetingscheduler.entity;

import java.util.List;
import java.util.Optional;

public class MeetingRequestObject {

    private String meetingDate;
    private String meetingStartTime;
    private String meetingEndTime;
    private int count;
    private int teamId;
    private int roomId;
    private String meetingName;
    private int meetingId;
    private List<Integer> addPeople;
    private List<Integer> removePeople;

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(String meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public String getMeetingEndTime() {
        return meetingEndTime;
    }

    public void setMeetingEndTime(String meetingEndTime) {
        this.meetingEndTime = meetingEndTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public List<Integer> getAddPeople() {
        return addPeople;
    }

    public void setAddPeople(List<Integer> addPeople) {
        this.addPeople = addPeople;
    }

    public List<Integer> getRemovePeople() {
        return removePeople;
    }

    public void setRemovePeople(List<Integer> removePeople) {
        this.removePeople = removePeople;
    }
}
