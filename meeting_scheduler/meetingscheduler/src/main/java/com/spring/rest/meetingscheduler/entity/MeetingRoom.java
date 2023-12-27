package com.spring.rest.meetingscheduler.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "meeting_room")
public class MeetingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_room_id")
    private int meetingRoomId;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "capacity")
    private int capacity;

    public MeetingRoom() {
    }

    public MeetingRoom(String roomName, int capacity) {
        this.roomName = roomName;
        this.capacity = capacity;
    }

    public int getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(int meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "MeetingRoom{" +
                "meetingRoomId=" + meetingRoomId +
                ", roomName='" + roomName + '\'' +
                ", capacity=" + capacity +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
